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

import com.cts.timer.client.RemarksClient;
import com.cts.timer.dao.TimeentryDao;
import com.cts.timer.dao.TimesheetsDao;
import com.cts.timer.dto.ApprovalRequestDTO;
import com.cts.timer.dto.RemarksDTO;
import com.cts.timer.dto.TimesheetResponseDTO;
import com.cts.timer.exception.ResourceNotFoundException;
import com.cts.timer.model.Timeentry;
import com.cts.timer.model.Timesheets;

/**
 * Service implementation for managing timesheets and time entries.
 */
@Service
public class TimesheetsServiceImpl implements TimesheetsService {

	private final TimesheetsDao timesheetsDao;
	private final TimeentryDao timeentryDao;
	private final RemarksClient remarksClient;

	private static final String TIMEENTRY_NOT_FOUND = "Timeentry not found";

	/**
	 * Constructor for TimesheetsServiceImpl.
	 *
	 * @param timesheetsDao DAO for timesheets.
	 * @param timeentryDao  DAO for time entries.
	 */
	@Autowired
	public TimesheetsServiceImpl(TimesheetsDao timesheetsDao, TimeentryDao timeentryDao, RemarksClient remarksClient) {
		this.timesheetsDao = timesheetsDao;
		this.timeentryDao = timeentryDao;
		this.remarksClient = remarksClient;
	}

	/**
	 * Updates the timesheet for a given date and employee.
	 *
	 * @param date       The date of the timesheet.
	 * @param employeeId The ID of the employee.
	 */
	@Override
	public void updateTimesheet(LocalDate date, Long employeeId) {
		Timesheets timesheet = timesheetsDao.findByEmployeeIdAndDate(employeeId, date).orElse(new Timesheets(null,
				employeeId, date, BigDecimal.ZERO, BigDecimal.ZERO, Timesheets.Status.PENDING, false, 0));
		List<Timeentry> timeEntries = timeentryDao.findByDateAndEmployeeId(date, employeeId);
		BigDecimal totalLoggedHrs = BigDecimal.ZERO;
		BigDecimal totalApprovedHrs = BigDecimal.ZERO;

		boolean allApproved = true;
		boolean allRejected = true;
		boolean allPending = true;
		boolean allSubmit = true;

		for (Timeentry entry : timeEntries) {
			if (entry.isSubmit()) {
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
		if (allApproved && allSubmit) {
			timesheet.setStatus(Timesheets.Status.APPROVED);
		} else if (allRejected && allSubmit) {
			timesheet.setStatus(Timesheets.Status.REJECTED);
		} else if (allPending && allSubmit) {
			timesheet.setStatus(Timesheets.Status.PENDING);
		} else {
			if (totalLoggedHrs.compareTo(totalApprovedHrs) > 0) {
				timesheet.setStatus(Timesheets.Status.PARTIALLY);
			}
		}

		timesheet.setSubmit(allSubmit);
		timesheetsDao.save(timesheet);
	}

	/**
	 * Retrieves the monthly timesheet for a given month, year, and employee.
	 *
	 * @param month      The month of the timesheet.
	 * @param year       The year of the timesheet.
	 * @param employeeId The ID of the employee.
	 * @return A list of timesheets for the specified month.
	 */

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

	/**
	 * Checks if a new time entry overlaps with existing entries.
	 *
	 * @param newEntry The new time entry to check.
	 * @return True if the new entry overlaps with existing entries, false
	 *         otherwise.
	 */

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

	/**
	 * Submits the timesheet for a given date and employee.
	 *
	 * @param employeeId The ID of the employee.
	 * @param date       The date of the timesheet.
	 * @return A message indicating the result of the submission.
	 */

	@Override
	public String submitTimesheet(Long employeeId, LocalDate date) {

		submitTimeentries(date, employeeId);
		updateTimesheet(date, employeeId);
		Optional<Timesheets> timesheet = timesheetsDao.findByEmployeeIdAndDate(employeeId, date);
		if (timesheet.isPresent()) {
			Timesheets ts = timesheet.get();
			ts.setSubmissionCount(ts.getSubmissionCount() + 1);
			timesheetsDao.save(ts);

		} else {
			return "No such timesheet not exists";
		}
		return "Submitted successfully";

	}

	/**
	 * Creates a new time entry.
	 *
	 * @param timeEntry The time entry to create.
	 * @return The created time entry.
	 */

	@Override
	public Timeentry createTimeEntry(Timeentry timeEntry) {

		if (isOverlapping(timeEntry)) {
			throw new IllegalArgumentException("Time entry overlaps with an existing entry.");
		}
		Timeentry savedTimeEntry = timeentryDao.save(timeEntry);
		updateTimesheet(timeEntry.getDate(), timeEntry.getEmployeeId());
		return savedTimeEntry;
	}

	/**
	 * Updates an existing time entry.
	 *
	 * @param id               The ID of the time entry to update.
	 * @param timeEntryDetails The new details of the time entry.
	 * @return The updated time entry.
	 */

	@Override
	public Timeentry updateTimeEntry(Long id, Timeentry timeEntryDetails) {
		Timeentry timeEntry = timeentryDao.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(TIMEENTRY_NOT_FOUND));

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

	/**
	 * Retrieves time entries for a given date and employee.
	 *
	 * @param date       The date of the time entries.
	 * @param employeeId The ID of the employee.
	 * @return A list of time entries for the specified date and employee.
	 */

	@Override
	public List<Timeentry> getTimeentriesOnDateAndEmployee(LocalDate date, Long employeeId) {
		return timeentryDao.findByDateAndEmployeeId(date, employeeId);
	}

	/**
	 * Deletes a time entry by its ID.
	 *
	 * @param id The ID of the time entry to delete.
	 */

	@Override
	public void deleteTimeEntry(Long id) {
		Timeentry timeentry = timeentryDao.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(TIMEENTRY_NOT_FOUND));
		timeentryDao.deleteById(id);
		updateTimesheet(timeentry.getDate(), timeentry.getEmployeeId());
	}

	/**
	 * Approves or rejects a list of time entries based on their IDs and the
	 * specified status.
	 *
	 * @param timeentryIds The list of time entry IDs to approve or reject.
	 * @param status       The status to set for the time entries (e.g., "APPROVED",
	 *                     "REJECTED").
	 */

	@Override
	public void approveReject(ApprovalRequestDTO request) {

		RemarksDTO remark = new RemarksDTO();
		remark.setTimesheetId(request.getTimesheetId());
		remark.setMessage(request.getMessage());
		remark.setCreatedAt(request.getCreatedAt());
		remark.setCreatedBy(request.getCreatedBy());

		for (Long id : request.getTimeentryIds()) {
			Timeentry timeentry = timeentryDao.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException(TIMEENTRY_NOT_FOUND));

			if (timeentry.getStatus() != Timeentry.Status.APPROVED && timeentry.isSubmit()) {
				timeentry.setStatus(Timeentry.Status.valueOf(request.getStatus().toUpperCase()));
				timeentryDao.save(timeentry);
				updateTimesheet(timeentry.getDate(), timeentry.getEmployeeId());
			}
		}

		if (request.getMessage() != null && !request.getMessage().isEmpty()) {
			remarksClient.createRemarks(remark);
		}

	}

	/**
	 * Retrieves time entries for a given date and employee.
	 *
	 * @param date       The date of the time entries.
	 * @param employeeId The ID of the employee.
	 * @return A list of time entries for the specified date and employee.
	 */

	@Override
	public TimesheetResponseDTO findByDateAndEmployeeId(LocalDate date, Long employeeId) {
		Optional<Timesheets> timesheetsOptional = timesheetsDao.findByEmployeeIdAndDate(employeeId, date);

		List<RemarksDTO> remarks = timesheetsOptional.map(ts -> {
			Long timesheetId = ts.getId();
			return remarksClient.getRemarksByTimesheetId(timesheetId).getBody();
		}).orElse(List.of());

		List<Timeentry> timeentries = timeentryDao.findByDateAndEmployeeId(date, employeeId);

		TimesheetResponseDTO response = new TimesheetResponseDTO();
		timesheetsOptional.ifPresent(response::setTimesheets); // Set the Timesheets object if present
		response.setTimeentries(timeentries);
		response.setRemarks(remarks);

		return response;
	}

	/**
	 * Submits all time entries for a given date and employee.
	 *
	 * @param date       The date of the time entries.
	 * @param employeeId The ID of the employee.
	 */

	@Override
	public void submitTimeentries(LocalDate date, Long employeeId) {
		List<Timeentry> timeentries = timeentryDao.findByDateAndEmployeeId(date, employeeId);
		for (Timeentry timeentry : timeentries) {
			if (!timeentry.isSubmit()) {
				timeentry.setSubmit(true);
				timeentryDao.save(timeentry);
			}
		}
	}

}
