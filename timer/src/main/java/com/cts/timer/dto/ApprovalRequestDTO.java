package com.cts.timer.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalRequestDTO {
	
	private List<Long> timeentryIds;
	private Long timesheetId;
    private String message;
    private String status;
    private LocalDateTime createdAt;
    private String createdBy;

}
