package com.cts.timer.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cts.timer.model.Timesheets;

@Repository
public interface TimesheetsDao extends JpaRepository<Timesheets, Long> {
	Optional<Timesheets> findByEmployeeIdAndDate(Long employeeId, LocalDate date);

	@Query("SELECT t FROM Timesheets t WHERE t.employeeId = :employeeId AND MONTH(t.date) = :month AND YEAR(t.date) = :year")
	List<Timesheets> findByEmployeeIdAndMonth(Long employeeId, int month, int year);
}
