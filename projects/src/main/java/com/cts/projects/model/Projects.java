package com.cts.projects.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "projects")
public class Projects {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false, unique = true)
	private String projName;

	@Column(nullable = false)
	private String type;

	@Column(nullable = false)
	private LocalDate startDate;

	@Column(nullable = false)
	private LocalDate closeDate;

	@Column(nullable = false, unique = true)
	private String TAM;

	@Column(nullable = false)
	private Integer estimatedhrs;

	@Column(nullable = false)
	private String description;
}
