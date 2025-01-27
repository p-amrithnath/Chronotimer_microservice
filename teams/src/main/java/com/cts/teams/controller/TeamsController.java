package com.cts.teams.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import com.cts.teams.model.Teams;
import com.cts.teams.service.TeamsService;

import java.util.Optional;

@RestController
@RequestMapping("/employees")
public class TeamsController {

    @Autowired
    private TeamsService employeeService;

    @GetMapping
    public Page<Teams> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        return employeeService.getAllTeams(page, size, sortBy);
    }

    @GetMapping("/{id}")
    public Optional<Teams> getEmployeeById(@PathVariable int id) {
        return employeeService.getTeamById(id);
    }

    @PostMapping
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
}