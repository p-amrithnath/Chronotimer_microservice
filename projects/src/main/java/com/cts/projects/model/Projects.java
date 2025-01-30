package com.cts.projects.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "projects")
public class Projects {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Project name is mandatory")
    @Size(max = 100, message = "Project name should not exceed 100 characters")
    private String projName;

    @Column(nullable = false)
    @NotBlank(message = "Type is mandatory")
    private String type;

    @Column(nullable = false)
    @NotNull(message = "Start date is mandatory")
    @PastOrPresent(message = "Start date cannot be in the future")
    private LocalDate startDate;

    @Column(nullable = false)
    @NotNull(message = "Close date is mandatory")
    @FutureOrPresent(message = "Close date cannot be in the past")
    private LocalDate closeDate;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "TAM is mandatory")
    private String TAM;

    @Column(nullable = false)
    @NotNull(message = "Estimated hours are mandatory")
    @Min(value = 1, message = "Estimated hours should be at least 1")
    private Integer estimatedhrs;

    @Column(nullable = false)
    @NotBlank(message = "Description is mandatory")
    private String description;
}