package com.cts.remarks.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.remarks.model.Remarks;
import com.cts.remarks.service.RemarksServiceImpl;

/**
 * REST controller for managing remarks. Provides endpoints for creating,
 * deleting, updating, and retrieving remarks.
 */
@RestController
@RequestMapping("/remarks")
public class RemarksController {

    private final RemarksServiceImpl remarksServiceImpl;

    @Autowired
    public RemarksController(RemarksServiceImpl remarksServiceImpl) {
        this.remarksServiceImpl = remarksServiceImpl;
    }

    /**
     * Creates a new remark.
     *
     * @param remarks the remark to create
     * @return the created remark
     */
    @PostMapping
    public ResponseEntity<Remarks> createRemarks(@RequestBody Remarks remarks) {
        Remarks createdRemarks = remarksServiceImpl.createRemarks(remarks);
        return new ResponseEntity<>(createdRemarks, HttpStatus.CREATED);
    }

    /**
     * Deletes a remark by ID.
     *
     * @param id the ID of the remark to delete
     * @return a response entity with no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRemarks(@PathVariable Integer id) {
        remarksServiceImpl.deleteRemarks(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Updates a remark by ID.
     *
     * @param id      the ID of the remark to update
     * @param updates the updates to apply to the remark
     * @return the updated remark
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Remarks> updateRemarks(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
        Remarks updatedRemarks = remarksServiceImpl.updateRemarks(id, updates);
        return new ResponseEntity<>(updatedRemarks, HttpStatus.OK);
    }

    /**
     * Retrieves a remark by ID.
     *
     * @param id the ID of the remark to retrieve
     * @return the retrieved remark
     */
    @GetMapping("/{id}")
    public ResponseEntity<Remarks> getRemarksById(@PathVariable Integer id) {
        Remarks remarks = remarksServiceImpl.getRemarksById(id);
        return new ResponseEntity<>(remarks, HttpStatus.OK);
    }

    /**
     * Retrieves all remarks.
     *
     * @return a list of all remarks
     */
    @GetMapping
    public ResponseEntity<List<Remarks>> getAllRemarks() {
        List<Remarks> remarks = remarksServiceImpl.getAllRemarks();
        return new ResponseEntity<>(remarks, HttpStatus.OK);
    }

    /**
     * Retrieves all remarks by timesheet ID.
     *
     * @param timesheetId the ID of the timesheet
     * @return a list of remarks for the specified timesheet
     */
    @GetMapping("/byTimesheetId/{timesheetId}")
    public ResponseEntity<List<Remarks>> getRemarksByTimesheetId(@PathVariable Long timesheetId) {
        List<Remarks> remarks = remarksServiceImpl.getRemarksByTimesheetId(timesheetId);
        return new ResponseEntity<>(remarks, HttpStatus.OK);
    }
}