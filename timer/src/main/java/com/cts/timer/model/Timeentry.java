package com.cts.timer.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "timeentry_info")
public class Timeentry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
