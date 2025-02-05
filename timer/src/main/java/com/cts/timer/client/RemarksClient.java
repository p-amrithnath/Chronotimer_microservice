package com.cts.timer.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.cts.timer.dto.RemarksDTO;

@FeignClient(name = "REMARKS")
public interface RemarksClient {

	@PostMapping("/remarks")
	public ResponseEntity<RemarksDTO> createRemarks(@RequestBody RemarksDTO remarks);
	
	@GetMapping("/remarks/byTimesheetId/{timesheetId}")
	public ResponseEntity<List<RemarksDTO>> getRemarksByTimesheetId(@PathVariable Long timesheetId);
	

}
