package com.cts.timer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cts.timer.dao.TimeentryDao;
import com.cts.timer.dao.TimesheetsDao;
import com.cts.timer.model.Timeentry;
import com.cts.timer.model.Timesheets;
import com.cts.timer.service.TimesheetsServiceImpl;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TimerApplicationTests {

	@Mock
	private TimesheetsDao timesheetsDao;

	@Mock
	private TimeentryDao timeentryDao;

	@InjectMocks
	private TimesheetsServiceImpl timesheetsService;

	private Timesheets timesheet;
	private Timeentry timeentry;

	@BeforeEach
	public void setUp() {
		timesheet = new Timesheets();
		timesheet.setId(1L);
		timesheet.setEmployeeId(1L);
		timesheet.setDate(LocalDate.now());
		timesheet.setLoggedHrs(BigDecimal.ZERO);
		timesheet.setApprovedHrs(BigDecimal.ZERO);
		timesheet.setStatus(Timesheets.Status.PENDING);
		timesheet.setSubmit(false);
		timesheet.setSubmission_count(0);

		timeentry = new Timeentry();
		timeentry.setId(1L);
		timeentry.setEmployeeId(1L);
		timeentry.setDate(LocalDate.now());
		timeentry.setStartTime(LocalDateTime.of(2025, 1, 27, 9, 0));
		timeentry.setEndTime(LocalDateTime.of(2025, 1, 27, 17, 0));
		timeentry.setHours(BigDecimal.valueOf(8));
		timeentry.setStatus(Timeentry.Status.PENDING);
		timeentry.setSubmit(true);
	}

	@Test
	public void testUpdateTimesheet() {
		when(timesheetsDao.findByEmployeeIdAndDate(timesheet.getEmployeeId(), timesheet.getDate()))
				.thenReturn(Optional.of(timesheet));
		when(timeentryDao.findByDateAndEmployeeId(timesheet.getDate(), timesheet.getEmployeeId()))
				.thenReturn(Arrays.asList(timeentry));

		timesheetsService.updateTimesheet(timesheet.getDate(), timesheet.getEmployeeId());

		verify(timesheetsDao, times(1)).save(timesheet);
	}

	@Test
	public void testGetMonthlyTimesheet() {
		when(timesheetsDao.findByEmployeeIdAndMonth(timesheet.getEmployeeId(), timesheet.getDate().getMonthValue(),
				timesheet.getDate().getYear())).thenReturn(Arrays.asList(timesheet));

		List<Timesheets> result = timesheetsService.getMonthlyTimesheet(timesheet.getDate().getMonthValue(),
				timesheet.getDate().getYear(), timesheet.getEmployeeId());

		assertFalse(result.isEmpty());
		verify(timesheetsDao, times(1)).findByEmployeeIdAndMonth(timesheet.getEmployeeId(),
				timesheet.getDate().getMonthValue(), timesheet.getDate().getYear());
	}

	@Test
	public void testIsOverlapping() {
		when(timeentryDao.findByDateAndEmployeeId(timeentry.getDate(), timeentry.getEmployeeId()))
				.thenReturn(Arrays.asList(timeentry));

		boolean result = timesheetsService.isOverlapping(timeentry);

		assertTrue(result);
	}

	@Test
	public void testSubmitTimesheet() {
		when(timesheetsDao.findByEmployeeIdAndDate(timesheet.getEmployeeId(), timesheet.getDate()))
				.thenReturn(Optional.of(timesheet));

		String result = timesheetsService.submitTimesheet(timesheet.getEmployeeId(), timesheet.getDate());

		assertEquals("Submitted successfully", result);
		verify(timesheetsDao, atLeastOnce()).save(timesheet);
	}

	@Test
	public void testCreateTimeEntry() {
		when(timeentryDao.save(timeentry)).thenReturn(timeentry);

		Timeentry result = timesheetsService.createTimeEntry(timeentry);

		assertEquals(timeentry.getId(), result.getId());
		verify(timeentryDao, times(1)).save(timeentry);
	}

	@Test
	public void testUpdateTimeEntry() {
		when(timeentryDao.findById(timeentry.getId())).thenReturn(Optional.of(timeentry));
		when(timeentryDao.save(timeentry)).thenReturn(timeentry);

		Timeentry result = timesheetsService.updateTimeEntry(timeentry.getId(), timeentry);

		assertEquals(timeentry.getId(), result.getId());
		verify(timeentryDao, times(1)).save(timeentry);
	}

	@Test
	public void testDeleteTimeEntry() {
		when(timeentryDao.findById(timeentry.getId())).thenReturn(Optional.of(timeentry));

		timesheetsService.deleteTimeEntry(timeentry.getId());

		verify(timeentryDao, times(1)).deleteById(timeentry.getId());
	}

	@Test
	public void testApproveReject() {
		when(timeentryDao.findById(timeentry.getId())).thenReturn(Optional.of(timeentry));

		timesheetsService.approveReject(Arrays.asList(timeentry.getId()), "APPROVED");

		verify(timeentryDao, atLeastOnce()).save(timeentry);
	}

	@Test
	public void testFindByDateAndEmployeeId() {
		when(timeentryDao.findByDateAndEmployeeId(timeentry.getDate(), timeentry.getEmployeeId()))
				.thenReturn(Arrays.asList(timeentry));

		List<Timeentry> result = timesheetsService.findByDateAndEmployeeId(timeentry.getDate(),
				timeentry.getEmployeeId());

		assertFalse(result.isEmpty());
		verify(timeentryDao, times(1)).findByDateAndEmployeeId(timeentry.getDate(), timeentry.getEmployeeId());
	}

	@Test
	public void testSubmitTimeentries() {
		timeentry.setSubmit(false);
		when(timeentryDao.findByDateAndEmployeeId(timeentry.getDate(), timeentry.getEmployeeId()))
				.thenReturn(Arrays.asList(timeentry));
		timesheetsService.submitTimeentries(timeentry.getDate(), timeentry.getEmployeeId());
		verify(timeentryDao, times(1)).save(timeentry);
		assertTrue(timeentry.isSubmit());
	}

}
