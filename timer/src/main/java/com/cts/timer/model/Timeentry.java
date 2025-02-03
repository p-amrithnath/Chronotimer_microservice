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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

	@NotNull(message = "Employee ID is mandatory")
	private Long employeeId;

	@NotNull(message = "Project ID is mandatory")
	private Long projectId;

	@NotBlank(message = "Category is mandatory")
	private String category;

	@NotBlank(message = "Task description is mandatory")
	private String taskDescription;

	@NotNull(message = "Start time is mandatory")
	private LocalDateTime startTime;

	@NotNull(message = "End time is mandatory")
	private LocalDateTime endTime;

	@NotNull(message = "Date is mandatory")
	private LocalDate date;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "Status is mandatory")
	private Status status;

	@NotNull(message = "Submit status is mandatory")
	private boolean submit;

	@NotNull(message = "Hours are mandatory")
	private BigDecimal hours;

	public enum Status {
		PENDING, APPROVED, REJECTED, PARTIALLY
	}
}