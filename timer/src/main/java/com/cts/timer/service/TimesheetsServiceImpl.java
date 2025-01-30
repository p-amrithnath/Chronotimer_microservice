package com.cts.timer.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.timer.dao.TimeentryDao;
import com.cts.timer.dao.TimesheetsDao;
import com.cts.timer.exception.ResourceNotFoundException;
import com.cts.timer.model.Timeentry;
import com.cts.timer.model.Timesheets;

@Service
public class TimesheetsServiceImpl implements TimesheetsService {

	@Autowired
	private TimesheetsDao timesheetsDao;

	@Autowired
	private TimeentryDao timeentryDao;

	@Override
	public void updateTimesheet(LocalDate date, Long employeeId) {
		Timesheets timesheet = timesheetsDao.findByEmployeeIdAndDate(employeeId, date).orElse(new Timesheets(null,
				employeeId, date, BigDecimal.ZERO, BigDecimal.ZERO, Timesheets.Status.PENDING, false, 0));
		List<Timeentry> timeEntries = findByDateAndEmployeeId(date, employeeId);
		BigDecimal totalLoggedHrs = BigDecimal.ZERO;
		BigDecimal totalApprovedHrs = BigDecimal.ZERO;

		boolean allApproved = true;
		boolean allRejected = true;
		boolean allPending = true;
		boolean allSubmit = true;

		for (Timeentry entry : timeEntries) {
			if (entry.getSubmit()) {
				totalLoggedHrs = totalLoggedHrs.add(entry.getHours());
				if (entry.getStatus() == Timeentry.Status.APPROVED) {
					totalApprovedHrs = totalApprovedHrs.add(entry.getHours());
					allRejected = false;
					allPending = false;
				} else if (entry.getStatus() == Timeentry.Status.REJECTED) {
					allApproved = false;
					allPending = false;
				} else if (entry.getStatus() == Timeentry.Status.PENDING) {
					allApproved = false;
					allRejected = false;
				}
			} else {
				allSubmit = false;
			}
		}

		timesheet.setApprovedHrs(totalApprovedHrs);
		timesheet.setLoggedHrs(totalLoggedHrs);

		if (allApproved) {
			timesheet.setStatus(Timesheets.Status.APPROVED);
		} else if (allRejected) {
			timesheet.setStatus(Timesheets.Status.REJECTED);
		} else if (allPending) {
			timesheet.setStatus(Timesheets.Status.PENDING);
		} else {
			if (totalLoggedHrs.compareTo(totalApprovedHrs) > 0) {
				timesheet.setStatus(Timesheets.Status.PARTIALLY);
			}
		}

		timesheet.setSubmit(allSubmit);
		timesheetsDao.save(timesheet);
	}

	@Override
	public List<Timesheets> getMonthlyTimesheet(int month, int year, Long employeeId) {
		List<Timesheets> monthlyTimesheet = new ArrayList<>();
		LocalDate startDate = LocalDate.of(year, month, 1);
		LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

		// Retrieve all timesheets for the given month and employee
		List<Timesheets> existingTimesheets = timesheetsDao.findByEmployeeIdAndMonth(employeeId, month, year);
		Map<LocalDate, Timesheets> timesheetMap = existingTimesheets.stream()
				.collect(Collectors.toMap(Timesheets::getDate, ts -> ts));

		for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
			monthlyTimesheet.add(timesheetMap.getOrDefault(date, new Timesheets(null, employeeId, date, BigDecimal.ZERO,
					BigDecimal.ZERO, Timesheets.Status.PENDING, false, 0)));
		}

		return monthlyTimesheet;
	}

	@Override
	public boolean isOverlapping(Timeentry newEntry) {
		List<Timeentry> existingEntries = timeentryDao.findByDateAndEmployeeId(newEntry.getDate(),
				newEntry.getEmployeeId());

		for (Timeentry entry : existingEntries) {
			if (newEntry.getStartTime().isBefore(entry.getEndTime())
					&& newEntry.getEndTime().isAfter(entry.getStartTime())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String submitTimesheet(Long employeeId, LocalDate date) {

		submitTimeentries(date, employeeId);
		updateTimesheet(date, employeeId);
		Optional<Timesheets> timesheet = timesheetsDao.findByEmployeeIdAndDate(employeeId, date);
		if (timesheet.isPresent()) {
			Timesheets ts = timesheet.get();
			ts.setSubmission_count(ts.getSubmission_count() + 1);
			timesheetsDao.save(ts);

		} else {
			return "No such timesheet not exists";
		}
		return "Submitted successfully";

	}

	@Override
	public Timeentry createTimeEntry(Timeentry timeEntry) {

		if (isOverlapping(timeEntry)) {
			throw new IllegalArgumentException("Time entry overlaps with an existing entry.");
		}
		Timeentry savedTimeEntry = timeentryDao.save(timeEntry);
		updateTimesheet(timeEntry.getDate(), timeEntry.getEmployeeId());
		return savedTimeEntry;
	}

	@Override
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
		updateTimesheet(updatedTimeEntry.getDate(), updatedTimeEntry.getEmployeeId());
		return updatedTimeEntry;
	}

	@Override
	public List<Timeentry> getTimeentriesOnDateAndEmployee(LocalDate date, Long employeeId) {
		return timeentryDao.findByDateAndEmployeeId(date, employeeId);
	}

	@Override
	public void deleteTimeEntry(Long id) {
		Timeentry timeentry = timeentryDao.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Timeentry not found"));
		timeentryDao.deleteById(id);
		updateTimesheet(timeentry.getDate(), timeentry.getEmployeeId());
	}

	@Override
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
			updateTimesheet(timeentry.getDate(), timeentry.getEmployeeId());
		}
	}

	@Override
	public List<Timeentry> findByDateAndEmployeeId(LocalDate date, Long employeeId) {
		return timeentryDao.findByDateAndEmployeeId(date, employeeId);
	}

	@Override
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
