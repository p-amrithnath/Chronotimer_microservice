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

@RestController
@RequestMapping("/timesheets")
public class TimesheetsController {

	@Autowired
	private TimesheetsService timesheetsService;

	@PostMapping("/update/{employeeId}/{date}")
	public void updateTimesheet(@PathVariable("date") LocalDate date, @PathVariable("employeeId") Long employeeId) {
		timesheetsService.updateTimesheet(date, employeeId);
	}
	
	
	@PatchMapping("/submit/{employeeId}/{date}")
	public String submitTimesheet(@PathVariable("employeeId") Long employeeId, @PathVariable("date") LocalDate date) {
		return timesheetsService.submitTimesheet(employeeId, date);
	}

	@GetMapping("/monthly/{month}/{year}/{employeeId}")
	public List<Timesheets> getMonthlyTimesheet(@PathVariable("month") int month, @PathVariable("year") int year,
			@PathVariable("employeeId") Long employeeId) {
		return timesheetsService.getMonthlyTimesheet(month, year, employeeId);
	}

}
