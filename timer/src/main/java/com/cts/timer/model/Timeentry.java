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
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
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
    @Min(value = 1, message = "Employee ID must be at least 1")
    private Long employeeId;

    @NotNull(message = "Project ID is mandatory")
    @Min(value = 1, message = "Project ID must be at least 1")
    private Long projectId;

    @NotBlank(message = "Category is mandatory")
    @Size(max = 50, message = "Category should not exceed 50 characters")
    private String category;

    @NotBlank(message = "Task description is mandatory")
    @Size(max = 255, message = "Task description should not exceed 255 characters")
    private String taskDescription;

    @NotNull(message = "Start time is mandatory")
    @PastOrPresent(message = "Start time cannot be in the future")
    private LocalDateTime startTime;

    @NotNull(message = "End time is mandatory")
    @FutureOrPresent(message = "End time cannot be in the past")
    private LocalDateTime endTime;

    @NotNull(message = "Date is mandatory")
    @PastOrPresent(message = "Date cannot be in the future")
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status is mandatory")
    private Status status;

    @NotNull(message = "Submit status is mandatory")
    private Boolean submit;

    @NotNull(message = "Hours are mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "Hours must be greater than 0")
    private BigDecimal hours;

    public enum Status {
        PENDING, APPROVED, REJECTED, PARTIALLY
    }
}