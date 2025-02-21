package com.cts.teams.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.teams.dto.EmailRequestDTO;
import com.cts.teams.model.Teams;
import com.cts.teams.service.TeamsService;
import com.cts.teams.utils.EmailService;

@RestController
@RequestMapping("/employees")
public class TeamsController {

	@Autowired
	private TeamsService employeeService;

	@Autowired
	private EmailService emailService;

	/**
	 * Retrieves a paginated list of all employees, sorted by the specified
	 * attribute.
	 * 
	 * @param page   the page number to retrieve
	 * @param size   the number of records per page
	 * @param sortBy the attribute to sort by
	 * @return a paginated list of employees
	 */
	@GetMapping
	public Page<Teams> getAllEmployees(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy) {
		return employeeService.getAllTeams(page, size, sortBy);
	}

	/**
	 * Retrieves an employee by their ID.
	 * 
	 * @param id the ID of the employee to retrieve
	 * @return an Optional containing the employee if found
	 */
	@GetMapping("/{id}")
	public Optional<Teams> getEmployeeById(@PathVariable int id) {
		return employeeService.getTeamById(id);
	}

	/**
	 * Creates a new employee.
	 * 
	 * @param employee the employee to create
	 * @return the created employee
	 */

	@PostMapping("/save")
	public Teams createEmployee(@RequestBody Teams employee) {
		return employeeService.createTeam(employee);
	}

	/**
	 * Updates an existing employee by their ID.
	 * 
	 * @param id              the ID of the employee to update
	 * @param employeeDetails the new details of the employee
	 * @return the updated employee
	 */

	@PutMapping("/{id}")
	public Teams updateEmployee(@PathVariable int id, @RequestBody Teams employeeDetails) {
		return employeeService.updateTeam(id, employeeDetails);
	}

	/**
	 * Deletes an employee by their ID.
	 * 
	 * @param id the ID of the employee to delete
	 */

	@DeleteMapping("/{id}")
	public void deleteEmployee(@PathVariable int id) {
		employeeService.deleteTeam(id);
	}

	@GetMapping("/send-email")
	public String sendEmail(@RequestBody EmailRequestDTO emailRequest) {
		emailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getText());
		return "Email sent successfully!";
	}
}