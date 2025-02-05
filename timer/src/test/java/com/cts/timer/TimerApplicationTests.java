package com.cts.timer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.springframework.http.ResponseEntity;

import com.cts.timer.client.RemarksClient;
import com.cts.timer.dao.TimeentryDao;
import com.cts.timer.dao.TimesheetsDao;
import com.cts.timer.dto.ApprovalRequestDTO;
import com.cts.timer.dto.RemarksDTO;
import com.cts.timer.dto.TimesheetResponseDTO;
import com.cts.timer.model.Timeentry;
import com.cts.timer.model.Timesheets;
import com.cts.timer.service.TimesheetsServiceImpl;

@ExtendWith(MockitoExtension.class)
class TimerApplicationTests {

	@Mock
	private TimesheetsDao timesheetsDao;

	@Mock
	private TimeentryDao timeentryDao;

	@Mock
	private RemarksClient remarksClient;

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
		timesheet.setSubmissionCount(0);

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
		assertEquals(timeentry, result);
		verify(timeentryDao, times(1)).save(timeentry);
	}

	@Test
	public void testUpdateTimeEntry() {
		when(timeentryDao.findById(timeentry.getId())).thenReturn(Optional.of(timeentry));
		when(timeentryDao.save(timeentry)).thenReturn(timeentry);

		System.out.println("ID:" + timeentry.getId());
		System.out.println("Timeentry:" + timeentry);

		Timeentry result = timesheetsService.updateTimeEntry(timeentry.getId(), timeentry);

		System.out.println("TimeEntry " + timeentry.getId());
		System.out.println("REsult " + result);

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

		ApprovalRequestDTO request = new ApprovalRequestDTO();
		request.setTimeentryIds(Arrays.asList(timeentry.getId()));
		request.setStatus("APPROVED");
		request.setTimesheetId(1L);
		request.setMessage("Approved");
		request.setCreatedAt(LocalDateTime.now());
		request.setCreatedBy("user1");

		timesheetsService.approveReject(request);

		verify(timeentryDao, atLeastOnce()).save(timeentry);
	}

	@Test
	public void testFindByDateAndEmployeeId() {
		// Mock the timeentryDao response
		when(timeentryDao.findByDateAndEmployeeId(timeentry.getDate(), timeentry.getEmployeeId()))
				.thenReturn(Arrays.asList(timeentry));

		// Mock the timesheetsDao response
		Timesheets timesheets = new Timesheets();
		timesheets.setId(1L);
		timesheets.setEmployeeId(timeentry.getEmployeeId());
		timesheets.setDate(timeentry.getDate());
		when(timesheetsDao.findByEmployeeIdAndDate(timeentry.getEmployeeId(), timeentry.getDate()))
				.thenReturn(Optional.of(timesheets));

		// Mock the remarksClient response
		RemarksDTO remark = new RemarksDTO();
		remark.setTimesheetId(timesheets.getId());
		remark.setMessage("Test remark");
		remark.setCreatedAt(LocalDateTime.now());
		remark.setCreatedBy("user1");
		when(remarksClient.getRemarksByTimesheetId(timesheets.getId()))
				.thenReturn(ResponseEntity.ok(Arrays.asList(remark)));

		// Call the service method
		TimesheetResponseDTO result = timesheetsService.findByDateAndEmployeeId(timeentry.getDate(),
				timeentry.getEmployeeId());

		// Verify the results
		assertNotNull(result);
		assertFalse(result.getTimeentries().isEmpty());
		assertFalse(result.getRemarks().isEmpty());
		assertEquals(1, result.getTimeentries().size());
		assertEquals(1, result.getRemarks().size());
		assertEquals(timeentry, result.getTimeentries().get(0));
		assertEquals(remark, result.getRemarks().get(0));

		// Verify the interactions
		verify(timeentryDao, times(1)).findByDateAndEmployeeId(timeentry.getDate(), timeentry.getEmployeeId());
		verify(timesheetsDao, times(1)).findByEmployeeIdAndDate(timeentry.getEmployeeId(), timeentry.getDate());
		verify(remarksClient, times(1)).getRemarksByTimesheetId(timesheets.getId());
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