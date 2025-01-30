package com.cts.timer.controller;

import java.time.LocalDate;
import java.util.List;

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
import com.cts.timer.model.Timeentry;
import com.cts.timer.service.TimesheetsService;

@RestController
@RequestMapping("/timeentry")
public class TimeentryController {

	@Autowired
	private TimesheetsService timeentryService;

	@GetMapping("/fetch/{employeeId}/{date}")
	public List<Timeentry> findByDateAndEmployeeId(@PathVariable("date") LocalDate date,
			@PathVariable("employeeId") Long employeeId) {

		return timeentryService.findByDateAndEmployeeId(date, employeeId);
	}

	@PatchMapping("/submit/{employeeId}/{date}")
	public void submitTimeentries(@PathVariable("date") LocalDate date, @PathVariable("employeeId") Long employeeId) {

		timeentryService.submitTimeentries(date, employeeId);
	}

	@PostMapping("/")
	public Timeentry createTimesheet(@RequestBody Timeentry timeentry) {
		return timeentryService.createTimeEntry(timeentry);

	}

	@GetMapping("/timeentry")
	public List<Timeentry> getTimeentriesOnDateAndEmployee(@RequestParam("date") LocalDate date,
			@RequestParam("employeeId") Long employeeId) {
		return timeentryService.getTimeentriesOnDateAndEmployee(date, employeeId);
	}

	@PatchMapping("/{id}")
	public String updateTimeEntry(@PathVariable Long id, @RequestBody Timeentry timeEntryDetails) {
		Timeentry updatedTimeEntry = timeentryService.updateTimeEntry(id, timeEntryDetails);
		if (updatedTimeEntry == null) {
			return "Already approved time entry";
		}
		return "Edited Successfully";
	}

	@DeleteMapping("/{id}")
	public void deleteTimeEntry(@PathVariable Long id) {
		timeentryService.deleteTimeEntry(id);

	}

	@PatchMapping("/approve-reject")
	public void approveReject(@RequestBody ApprovalRequestDTO request) {
		timeentryService.approveReject(request.getTimeentryIds(), request.getStatus());
	}

}
