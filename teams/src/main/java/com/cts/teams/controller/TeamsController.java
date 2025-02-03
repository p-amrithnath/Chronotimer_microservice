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

	@GetMapping
	public Page<Teams> getAllEmployees(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy) {
		return employeeService.getAllTeams(page, size, sortBy);
	}

	@GetMapping("/{id}")
	public Optional<Teams> getEmployeeById(@PathVariable int id) {
		return employeeService.getTeamById(id);
	}

	@PostMapping("/save")
	public Teams createEmployee(@RequestBody Teams employee) {
		return employeeService.createTeam(employee);
	}

	@PutMapping("/{id}")
	public Teams updateEmployee(@PathVariable int id, @RequestBody Teams employeeDetails) {
		return employeeService.updateTeam(id, employeeDetails);
	}

	@DeleteMapping("/{id}")
	public void deleteEmployee(@PathVariable int id) {
		employeeService.deleteTeam(id);
	}

	@GetMapping("/send-email")
	public String sendEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String text) {
		emailService.sendEmail(to, subject, text);
		return "Email sent successfully!";
	}
}