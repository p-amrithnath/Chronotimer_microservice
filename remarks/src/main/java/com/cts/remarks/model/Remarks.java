package com.cts.remarks.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Entity class representing a remark.
 */
@Entity
@Table(name = "remarks")
@Data
public class Remarks {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int remarkId;

	@Column(nullable = false)
	private Long timesheetId;

	@Column(nullable = false)
	private String message;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private String createdBy;

}
