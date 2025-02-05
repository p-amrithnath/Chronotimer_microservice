package com.cts.remarks.service;

import java.util.List;
import java.util.Map;

import com.cts.remarks.model.Remarks;

public interface RemarksService {

	public abstract Remarks createRemarks(Remarks remarks);

	public abstract void deleteRemarks(int id);

	public abstract Remarks updateRemarks(int id, Map<String, Object> updates);

	public abstract Remarks getRemarksById(int id);

	public abstract List<Remarks> getAllRemarks();

	public abstract List<Remarks> getRemarksByTimesheetId(Long timesheetId);
}