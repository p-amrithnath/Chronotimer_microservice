package com.cts.timeentry.service;

import java.time.LocalDate;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("TIMESHEETS")
public interface TimesheetsClientInterface {

	@PostMapping("timesheets/update/{employeeId}/{date}")
	public void updateTimesheet(@PathVariable("date") LocalDate date, @PathVariable("employeeId") Long employeeId);

}
