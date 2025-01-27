package com.cts.timesheets.model;

import java.math.BigDecimal;
import java.time.LocalDate;

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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "timesheet_info")
public class Timesheets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employeeId;
    private LocalDate date;
    private BigDecimal loggedHrs;
    private BigDecimal approvedHrs;
  
    @Enumerated(EnumType.STRING)
    private Status status;

    private Boolean submit;
    
    private int submission_count;
    
    public enum Status {
        PENDING,
        APPROVED,
        REJECTED,
        PARTIALLY
    }

}
