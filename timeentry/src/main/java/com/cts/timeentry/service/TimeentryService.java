package com.cts.timeentry.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.timeentry.dao.TimeentryDao;
import com.cts.timeentry.exception.ResourceNotFoundException;
import com.cts.timeentry.model.Timeentry;

@Service
public class TimeentryService {

	@Autowired
	private TimeentryDao timeentryDao;

	@Autowired
	private TimesheetsClientInterface timesheetsClientInterface;

	public Timeentry createTimeEntry(Timeentry timeEntry) {
		Timeentry savedTimeEntry = timeentryDao.save(timeEntry);
		timesheetsClientInterface.updateTimesheet(timeEntry.getDate(), timeEntry.getEmployeeId());
		return savedTimeEntry;
	}

	public Timeentry updateTimeEntry(Long id, Timeentry timeEntryDetails) {
		Timeentry timeEntry = timeentryDao.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Timeentry not found"));

		if (timeEntry.getStatus() == Timeentry.Status.APPROVED) {
			return null;
		}

		timeEntry.setEmployeeId(timeEntryDetails.getEmployeeId());
		timeEntry.setProjectId(timeEntryDetails.getProjectId());
		timeEntry.setCategory(timeEntryDetails.getCategory());
		timeEntry.setTaskDescription(timeEntryDetails.getTaskDescription());
		timeEntry.setStartTime(timeEntryDetails.getStartTime());
		timeEntry.setEndTime(timeEntryDetails.getEndTime());
		timeEntry.setStatus(Timeentry.Status.PENDING);
		timeEntry.setSubmit(false);
		timeEntry.setHours(timeEntryDetails.getHours());

		Timeentry updatedTimeEntry = timeentryDao.save(timeEntry);
		timesheetsClientInterface.updateTimesheet(updatedTimeEntry.getDate(), updatedTimeEntry.getEmployeeId());
		return updatedTimeEntry;
	}

	public List<Timeentry> getTimeentriesOnDateAndEmployee(LocalDate date, Long employeeId) {
		return timeentryDao.findByDateAndEmployeeId(date, employeeId);
	}

	public void deleteTimeEntry(Long id) {
		Timeentry timeentry = timeentryDao.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Timeentry not found"));
		timeentryDao.deleteById(id);
		timesheetsClientInterface.updateTimesheet(timeentry.getDate(), timeentry.getEmployeeId());
	}

	public void approveReject(List<Long> timeentryIds, String status) {
		for (Long id : timeentryIds) {
			Timeentry timeentry = timeentryDao.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Timeentry not found"));

			if (timeentry.getStatus() == Timeentry.Status.APPROVED) {
				continue;
			}
			if (timeentry.getSubmit()) {
				timeentry.setStatus(Timeentry.Status.valueOf(status.toUpperCase()));
			} else {
				continue;
			}
			timeentryDao.save(timeentry);
			timesheetsClientInterface.updateTimesheet(timeentry.getDate(), timeentry.getEmployeeId());
		}
	}

	public List<Timeentry> findByDateAndEmployeeId(LocalDate date, Long employeeId) {
		return timeentryDao.findByDateAndEmployeeId(date, employeeId);
	}

	public void submitTimeentries(LocalDate date, Long employeeId) {
		List<Timeentry> timeentries = findByDateAndEmployeeId(date, employeeId);
		for (Timeentry timeentry : timeentries) {
			if (timeentry.getSubmit()) {
				continue;
			} else {
				timeentry.setSubmit(true);
				timeentryDao.save(timeentry);
			}
		}

	}

}
