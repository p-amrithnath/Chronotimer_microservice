package com.cts.timer.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeentryDTO {
	private Long id;

	private Long employeeId;
	private Long projectId;
	private String category;
	private String taskDescription;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private LocalDate date;
	@Enumerated(EnumType.STRING)
	private Status status;

	private Boolean submit;
	private BigDecimal hours;

	public enum Status {
		PENDING, APPROVED, REJECTED, PARTIALLY

	}

}
