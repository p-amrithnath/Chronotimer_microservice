package com.cts.timesheets.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.timesheets.dao.TimesheetsDao;
import com.cts.timesheets.dto.TimeentryDTO;
import com.cts.timesheets.model.Timesheets;

@Service
public class TimesheetsService {

	@Autowired
	private TimesheetsDao timesheetsDao;

	@Autowired
	private TimeenrtyClientInterface timeenrtyClientInterface;

	public void updateTimesheet(LocalDate date, Long employeeId) {
		Timesheets timesheet = timesheetsDao.findByEmployeeIdAndDate(employeeId, date).orElse(new Timesheets(null,
				employeeId, date, BigDecimal.ZERO, BigDecimal.ZERO, Timesheets.Status.PENDING, false, 0));
		List<TimeentryDTO> timeEntries = timeenrtyClientInterface.findByDateAndEmployeeId(date, employeeId);
		BigDecimal totalLoggedHrs = BigDecimal.ZERO;
		BigDecimal totalApprovedHrs = BigDecimal.ZERO;

		boolean allApproved = true;
		boolean allRejected = true;
		boolean allPending = true;
		boolean allSubmit = true;

		for (TimeentryDTO entry : timeEntries) {
			if (entry.getSubmit()) {
				totalLoggedHrs = totalLoggedHrs.add(entry.getHours());
				if (entry.getStatus() == TimeentryDTO.Status.APPROVED) {
					totalApprovedHrs = totalApprovedHrs.add(entry.getHours());
					allRejected = false;
					allPending = false;
				} else if (entry.getStatus() == TimeentryDTO.Status.REJECTED) {
					allApproved = false;
					allPending = false;
				} else if (entry.getStatus() == TimeentryDTO.Status.PENDING) {
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

	public String submitTimesheet(Long employeeId, LocalDate date) {

		timeenrtyClientInterface.submitTimeentries(date, employeeId);
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

}
