package com.cts.timer.dto;


import java.util.List;

import com.cts.timer.model.Timeentry;
import com.cts.timer.model.Timesheets;

import lombok.Data;

/**
 * Custom response object to hold timesheet data and remarks.
 */
@Data
public class TimesheetResponseDTO {
    private Timesheets timesheets;
    private List<Timeentry> timeentries;
    private List<RemarksDTO> remarks;
}