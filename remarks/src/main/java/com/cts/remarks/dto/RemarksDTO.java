package com.cts.remarks.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Remarks.
 */
@Data
public class RemarksDTO {

    private Long timesheetId;
    private String message;
    private LocalDateTime createdAt;
    private String createdBy;
}