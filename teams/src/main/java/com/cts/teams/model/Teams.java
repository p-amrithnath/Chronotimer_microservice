package com.cts.teams.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Teams {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

//	@Column(nullable = false, unique = true)
	private String empId;

	@Column(nullable = false)
	private String name;

//	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String empDesg;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@Column(nullable = false)
	private BigDecimal salary;

	@Column(nullable = false)
	private LocalDate doj;

	public enum Role {
		EMPLOYEE, SUPERADMIN
	}
}