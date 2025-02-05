package com.cts.remarks.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.remarks.model.Remarks;

@Repository
public interface RemarksDao extends JpaRepository<Remarks, Integer> {

	List<Remarks> findByTimesheetId(Long timesheetId);

}
