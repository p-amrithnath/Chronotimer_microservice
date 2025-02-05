package com.cts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cts.remarks.dao.RemarksDao;
import com.cts.remarks.exception.ResourceNotFoundException;
import com.cts.remarks.model.Remarks;
import com.cts.remarks.service.RemarksServiceImpl;

@ExtendWith(MockitoExtension.class)
public class RemarksApplicationTests {

	@Mock
	private RemarksDao remarksDao;

	@InjectMocks
	private RemarksServiceImpl remarksService;

	private Remarks remark;

	@BeforeEach
	public void setUp() {
		remark = new Remarks();
		remark.setRemarkId(1);
		remark.setTimesheetId(100L);
		remark.setMessage("Test message");
		remark.setCreatedAt(LocalDateTime.now());
		remark.setCreatedBy("TestUser");
	}

	@Test
	public void testCreateRemarks() {
		when(remarksDao.save(remark)).thenReturn(remark);

		Remarks createdRemark = remarksService.createRemarks(remark);

		assertEquals(remark.getRemarkId(), createdRemark.getRemarkId());
		verify(remarksDao, times(1)).save(remark);
	}

	@Test
	public void testDeleteRemarks() {
		when(remarksDao.existsById(remark.getRemarkId())).thenReturn(true);

		remarksService.deleteRemarks(remark.getRemarkId());

		verify(remarksDao, times(1)).deleteById(remark.getRemarkId());
	}

	@Test
	public void testDeleteRemarks_NotFound() {
		when(remarksDao.existsById(remark.getRemarkId())).thenReturn(false);

		assertThrows(ResourceNotFoundException.class, () -> remarksService.deleteRemarks(remark.getRemarkId()));
	}

	@Test
	public void testUpdateRemarks() {
		when(remarksDao.findById(remark.getRemarkId())).thenReturn(Optional.of(remark));
		when(remarksDao.save(remark)).thenReturn(remark);

		Remarks updatedRemark = remarksService.updateRemarks(remark.getRemarkId(), Map.of());

		assertEquals(remark.getRemarkId(), updatedRemark.getRemarkId());
		verify(remarksDao, times(1)).save(remark);
	}

	@Test
	public void testUpdateRemarks_NotFound() {
		when(remarksDao.findById(remark.getRemarkId())).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () ->
			remarksService.updateRemarks(remark.getRemarkId(), Map.of()));
	}

	@Test
	public void testGetRemarksById() {
		when(remarksDao.findById(remark.getRemarkId())).thenReturn(Optional.of(remark));

		Remarks foundRemark = remarksService.getRemarksById(remark.getRemarkId());

		assertEquals(remark.getRemarkId(), foundRemark.getRemarkId());
	}

	@Test
	public void testGetRemarksById_NotFound() {
		when(remarksDao.findById(remark.getRemarkId())).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> 
			remarksService.getRemarksById(remark.getRemarkId()));
	}

	@Test
	public void testGetAllRemarks() {
		List<Remarks> remarksList = Arrays.asList(remark);
		when(remarksDao.findAll()).thenReturn(remarksList);

		List<Remarks> allRemarks = remarksService.getAllRemarks();

		assertEquals(1, allRemarks.size());
		assertEquals(remark.getRemarkId(), allRemarks.get(0).getRemarkId());
	}

	@Test
	public void testGetRemarksByTimesheetId() {
		List<Remarks> remarksList = Arrays.asList(remark);
		when(remarksDao.findByTimesheetId(100L)).thenReturn(remarksList);

		List<Remarks> remarksByTimesheetId = remarksService.getRemarksByTimesheetId(100L);

		assertEquals(1, remarksByTimesheetId.size());
		assertEquals(remark.getRemarkId(), remarksByTimesheetId.get(0).getRemarkId());
	}
}