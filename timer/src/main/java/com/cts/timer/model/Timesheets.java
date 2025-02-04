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
import jakarta.validation.constraints.NotNull;
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
	private Long employeeId;

	@NotNull(message = "Date is mandatory")
	private LocalDate date;

	private BigDecimal loggedHrs;


	private BigDecimal approvedHrs;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "Status is mandatory")
	private Status status;

	@NotNull(message = "Submit status is mandatory")
	private boolean submit;


	private int submissionCount;

	public enum Status {
		PENDING, APPROVED, REJECTED, PARTIALLY
	}

}