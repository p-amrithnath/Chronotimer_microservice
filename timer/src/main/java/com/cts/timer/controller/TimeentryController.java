package com.cts.timer.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.timer.dto.ApprovalRequestDTO;
import com.cts.timer.dto.TimesheetResponseDTO;
import com.cts.timer.model.Timeentry;
import com.cts.timer.service.TimesheetsService;

/**
 * REST controller for managing time entries.
 */

@RestController
@RequestMapping("/timeentry")
public class TimeentryController {

	private final TimesheetsService timeentryService;

	/**
	 * Constructor for TimeentryController.
	 *
	 * @param timeentryService Service for managing timesheets and time entries.
	 */
	@Autowired
	public TimeentryController(TimesheetsService timeentryService) {
		this.timeentryService = timeentryService;
	}

	/**
	 * Retrieves time entries for a given date and employee.
	 *
	 * @param date       The date of the time entries.
	 * @param employeeId The ID of the employee.
	 * @return A list of time entries for the specified date and employee.
	 */

	@GetMapping("/fetch/{employeeId}/{date}")
	public TimesheetResponseDTO findByDateAndEmployeeId(@PathVariable("date") LocalDate date,
			@PathVariable("employeeId") Long employeeId) {

		return timeentryService.findByDateAndEmployeeId(date, employeeId);
	}

	/**
	 * Submits all time entries for a given date and employee.
	 *
	 * @param date       The date of the time entries.
	 * @param employeeId The ID of the employee.
	 */

	@PatchMapping("/submit/{employeeId}/{date}")
	public void submitTimeentries(@PathVariable("date") LocalDate date, @PathVariable("employeeId") Long employeeId) {
		timeentryService.submitTimeentries(date, employeeId);
	}

	/**
	 * Creates a new time entry.
	 *
	 * @param timeentry The time entry to create.
	 * @return The created time entry.
	 */

	@PostMapping("/")
	public Timeentry createTimesheet(@RequestBody Timeentry timeentry) {
		return timeentryService.createTimeEntry(timeentry);

	}

	/**
	 * Retrieves time entries for a given date and employee.
	 *
	 * @param date       The date of the time entries.
	 * @param employeeId The ID of the employee.
	 * @return A list of time entries for the specified date and employee.
	 */

	@GetMapping("/timeentry")
	public TimesheetResponseDTO getTimeentriesOnDateAndEmployee(@RequestParam("date") LocalDate date,
			@RequestParam("employeeId") Long employeeId) {
		return timeentryService.findByDateAndEmployeeId(date, employeeId);
	}

	/**
	 * Updates an existing time entry.
	 *
	 * @param id               The ID of the time entry to update.
	 * @param timeEntryDetails The new details of the time entry.
	 * @return A message indicating the result of the update.
	 */

	@PatchMapping("/{id}")
	public String updateTimeEntry(@PathVariable Long id, @RequestBody Timeentry timeEntryDetails) {
		Timeentry updatedTimeEntry = timeentryService.updateTimeEntry(id, timeEntryDetails);
		if (updatedTimeEntry == null) {
			return "Already approved time entry";
		}
		return "Edited Successfully";
	}

	/**
	 * Deletes a time entry by its ID.
	 *
	 * @param id The ID of the time entry to delete.
	 */

	@DeleteMapping("/{id}")
	public void deleteTimeEntry(@PathVariable Long id) {
		timeentryService.deleteTimeEntry(id);

	}

	/**
	 * Approves or rejects a list of time entries based on their IDs and the
	 * specified status.
	 *
	 * @param request The request containing the list of time entry IDs and the
	 *                status.
	 */

	@PatchMapping("/approve-reject")
	public void approveReject(@RequestBody ApprovalRequestDTO request) {
		timeentryService.approveReject(request);
	}

}
