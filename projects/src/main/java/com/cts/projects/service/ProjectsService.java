package com.cts.projects.service;

import java.util.List;

import com.cts.projects.model.Projects;

public interface ProjectsService {
	Projects saveProject(Projects project);

	Projects updateProject(Projects project);

	void deleteProject(int id);

	List<Projects> getAllProjects();

	Projects getProjectById(int id);
}