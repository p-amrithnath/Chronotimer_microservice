package com.cts.timer.service;

import java.time.LocalDate;
import java.util.List;

import com.cts.timer.dto.ApprovalRequestDTO;
import com.cts.timer.dto.TimesheetResponseDTO;
import com.cts.timer.model.Timeentry;
import com.cts.timer.model.Timesheets;

public interface TimesheetsService {

	public abstract void updateTimesheet(LocalDate date, Long employeeId);

	public abstract List<Timesheets> getMonthlyTimesheet(int month, int year, Long employeeId);

	public abstract boolean isOverlapping(Timeentry newEntry);

	public abstract String submitTimesheet(Long employeeId, LocalDate date);

	public abstract Timeentry createTimeEntry(Timeentry timeEntry);

	public abstract Timeentry updateTimeEntry(Long id, Timeentry timeEntryDetails);

	public abstract List<Timeentry> getTimeentriesOnDateAndEmployee(LocalDate date, Long employeeId);

	public abstract void deleteTimeEntry(Long id);

	public abstract void approveReject(ApprovalRequestDTO request);

	public abstract TimesheetResponseDTO findByDateAndEmployeeId(LocalDate date, Long employeeId);

	public abstract void submitTimeentries(LocalDate date, Long employeeId);
}
