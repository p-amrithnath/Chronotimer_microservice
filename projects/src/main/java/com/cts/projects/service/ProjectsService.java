package com.cts.projects.service;

import java.util.List;

/**
 * Interface for the Projects service
 */

import com.cts.projects.model.Projects;

public interface ProjectsService {
	public abstract Projects saveProject(Projects project);

	public abstract Projects updateProject(Projects project);

	public abstract void deleteProject(int id);

	public abstract List<Projects> getAllProjects();

	public abstract Projects getProjectById(int id);
}