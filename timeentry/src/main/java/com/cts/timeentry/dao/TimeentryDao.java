package com.cts.timeentry.dao;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.timeentry.model.Timeentry;

@Repository
public interface TimeentryDao extends JpaRepository<Timeentry, Long> {
	List<Timeentry> findByDateAndEmployeeId(LocalDate date, Long employeeId);
}