package com.cts.timesheets.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cts.timesheets.dto.TimeentryDTO;

@FeignClient("TIMEENTRY")
public interface TimeenrtyClientInterface {

	@GetMapping("timeentry/fetch/{employeeId}/{date}")
	public List<TimeentryDTO> findByDateAndEmployeeId(@PathVariable("date") LocalDate date,
			@PathVariable("employeeId") Long employeeId);

	@PatchMapping("timeentry/submit/{employeeId}/{date}")
	public void submitTimeentries(@PathVariable("date") LocalDate date, @PathVariable("employeeId") Long employeeId);
}
