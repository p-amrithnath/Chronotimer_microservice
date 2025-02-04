package com.cts.timer.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.timer.model.Timesheets;
import com.cts.timer.service.TimesheetsService;

/**
 * REST controller for managing timesheets.
 */

@RestController
@RequestMapping("/timesheets")
public class TimesheetsController {

	private final TimesheetsService timesheetsService;

	/**
	 * Constructor for TimesheetsController.
	 *
	 * @param timesheetsService Service for managing timesheets.
	 */

	@Autowired
	public TimesheetsController(TimesheetsService timesheetsService) {
		this.timesheetsService = timesheetsService;
	}

	/**
	 * Updates the timesheet for a given date and employee.
	 *
	 * @param date       The date of the timesheet.
	 * @param employeeId The ID of the employee.
	 */

	@PostMapping("/update/{employeeId}/{date}")
	public void updateTimesheet(@PathVariable("date") LocalDate date, @PathVariable("employeeId") Long employeeId) {
		timesheetsService.updateTimesheet(date, employeeId);
	}

	/**
	 * Submits the timesheet for a given date and employee.
	 *
	 * @param employeeId The ID of the employee.
	 * @param date       The date of the timesheet.
	 * @return A message indicating the result of the submission.
	 */

	@PatchMapping("/submit/{employeeId}/{date}")
	public String submitTimesheet(@PathVariable("employeeId") Long employeeId, @PathVariable("date") LocalDate date) {
		return timesheetsService.submitTimesheet(employeeId, date);
	}

	/**
	 * Retrieves the monthly timesheet for a given month, year, and employee.
	 *
	 * @param month      The month of the timesheet.
	 * @param year       The year of the timesheet.
	 * @param employeeId The ID of the employee.
	 * @return A list of timesheets for the specified month.
	 */

	@GetMapping("/monthly/{month}/{year}/{employeeId}")
	public List<Timesheets> getMonthlyTimesheet(@PathVariable("month") int month, @PathVariable("year") int year,
			@PathVariable("employeeId") Long employeeId) {
		return timesheetsService.getMonthlyTimesheet(month, year, employeeId);
	}

}
