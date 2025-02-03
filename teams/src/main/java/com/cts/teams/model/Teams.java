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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Entity
public class Teams {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Employee ID is mandatory")
    private String empId;

    @NotBlank(message = "Name is mandatory")
    @Column(nullable = false)
    private String name;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Designation is mandatory")
    @Column(nullable = false)
    private String empDesg;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Role is mandatory")
    @Column(nullable = false)
    private Role role;

    @Positive(message = "Salary must be positive")
    @NotNull(message = "Salary is mandatory")
    @Column(nullable = false)
    private BigDecimal salary;

    @PastOrPresent(message = "Date of joining must be in the past or present")
    @NotNull(message = "Date of joining is mandatory")
    @Column(nullable = false)
    private LocalDate doj;

    public enum Role {
        EMPLOYEE, SUPERADMIN
    }
}