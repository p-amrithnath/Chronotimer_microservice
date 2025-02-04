package com.cts.projects.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.projects.model.Projects;
import com.cts.projects.service.ProjectsService;

/**
 * REST controller for managing project entities. Provides endpoints for
 * creating, updating, deleting, and retrieving projects.
 */
@RestController
@RequestMapping("/projects")
public class ProjectsController {

	private final ProjectsService projectService;

	@Autowired
	public ProjectsController(ProjectsService projectService) {
		this.projectService = projectService;
	}

	/**
	 * Endpoint to save a new project.
	 *
	 * @param project the project to be saved
	 * @return the saved project
	 */
	@PostMapping("/save")
	public Projects saveProject(@RequestBody Projects project) {
		return projectService.saveProject(project);
	}

	/**
	 * Endpoint to update an existing project.
	 *
	 * @param project the project to be updated
	 * @return the updated project
	 */
	@PutMapping("/edit")
	public Projects updateProject(@RequestBody Projects project) {
		return projectService.updateProject(project);
	}

	/**
	 * Endpoint to delete a project by its ID.
	 *
	 * @param id the ID of the project to be deleted
	 * @return null
	 */
	@DeleteMapping("/delete/{id}")
	public Void deleteProject(@PathVariable int id) {
		projectService.deleteProject(id);
		return null;
	}

	/**
	 * Endpoint to retrieve all projects.
	 *
	 * @return a list of all projects
	 */
	@GetMapping("/getAll")
	public List<Projects> getAllProjects() {
		return projectService.getAllProjects();
	}

	/**
	 * Endpoint to retrieve a project by its ID.
	 *
	 * @param id the ID of the project to be retrieved
	 * @return the project with the specified ID
	 */
	@GetMapping("/getById/{id}")
	public Projects getProjectById(@PathVariable int id) {
		return projectService.getProjectById(id);
	}
}