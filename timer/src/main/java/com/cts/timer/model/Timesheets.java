package com.cts.timer.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "timesheet_info")
public class Timesheets {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Employee ID is mandatory")
	@Min(value = 1, message = "Employee ID must be at least 1")
	private Long employeeId;

	@NotNull(message = "Date is mandatory")
	@PastOrPresent(message = "Date cannot be in the future")
	private LocalDate date;

	@NotNull(message = "Logged hours are mandatory")
	@DecimalMin(value = "0.0", inclusive = false, message = "Logged hours must be greater than 0")
	private BigDecimal loggedHrs;

	@NotNull(message = "Approved hours are mandatory")
	@DecimalMin(value = "0.0", inclusive = false, message = "Approved hours must be greater than 0")
	private BigDecimal approvedHrs;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "Status is mandatory")
	private Status status;

	@NotNull(message = "Submit status is mandatory")
	private Boolean submit;

	@Max(value = 0, message = "Submission count must be at most 2")
	private int submission_count;

	public enum Status {
		PENDING, APPROVED, REJECTED, PARTIALLY
	}

}