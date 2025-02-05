package com.cts.remarks.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.remarks.dao.RemarksDao;
import com.cts.remarks.exception.ResourceNotFoundException;
import com.cts.remarks.model.Remarks;

/**
 * Service class for managing remarks. Provides methods for creating, deleting,
 * updating, and retrieving remarks.
 */
@Service
public class RemarksServiceImpl implements RemarksService {

	private final RemarksDao remarksDao;

	@Autowired
	public RemarksServiceImpl(RemarksDao remarksDao) {
		this.remarksDao = remarksDao;
	}

	public static final String MSG = "Remark not found with id ";

	/**
	 * Creates a new remark.
	 *
	 * @param remarks the remark to create
	 * @return the created remark
	 */
	@Override
	public Remarks createRemarks(Remarks remarks) {
		return remarksDao.save(remarks);
	}

	/**
	 * Deletes a remark by ID.
	 *
	 * @param id the ID of the remark to delete
	 */
	@Override
	public void deleteRemarks(int id) {
		if (!remarksDao.existsById(id)) {
			throw new ResourceNotFoundException(MSG + id);
		}
		remarksDao.deleteById(id);
	}

	/**
	 * Updates a remark by ID.
	 *
	 * @param id      the ID of the remark to update
	 * @param updates the updates to apply to the remark
	 * @return the updated remark
	 */
	@Override
	public Remarks updateRemarks(int id, Map<String, Object> updates) {
		return remarksDao.findById(id).map(remarksDao::save)
				.orElseThrow(() -> new ResourceNotFoundException(MSG + id));
	}

	/**
	 * Retrieves a remark by ID.
	 *
	 * @param id the ID of the remark to retrieve
	 * @return the retrieved remark
	 */
	@Override
	public Remarks getRemarksById(int id) {
		return remarksDao.findById(id).orElseThrow(() -> new ResourceNotFoundException(MSG + id));
	}

	/**
	 * Retrieves all remarks.
	 *
	 * @return a list of all remarks
	 */
	@Override
	public List<Remarks> getAllRemarks() {
		return remarksDao.findAll();
	}

	/**
	 * Retrieves all remarks by timesheet ID.
	 *
	 * @param timesheetId the ID of the timesheet
	 * @return a list of remarks for the specified timesheet
	 */
	@Override
	public List<Remarks> getRemarksByTimesheetId(Long timesheetId) {
		return remarksDao.findByTimesheetId(timesheetId);
	}
}