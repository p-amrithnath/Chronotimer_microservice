package com.cts.projects.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.projects.dao.ProjectsDao;
import com.cts.projects.exception.ResourceNotFoundException;
import com.cts.projects.model.Projects;

/**
 * Implementation of the ProjectsService interface
 */
@Service
public class ProjectsServiceImpl implements ProjectsService {

	
	private final ProjectsDao projectDao;
	
	private static final String PROJECT_NOT_FOUND = "Project not found with id: ";
	
	@Autowired
	public ProjectsServiceImpl(ProjectsDao projectDao) {
		this.projectDao = projectDao;
	}

	/**
	 * Saves a new project.
	 *
	 * @param project the project to be saved
	 * @return the saved project
	 */
	@Override
	public Projects saveProject(Projects project) {
		return projectDao.save(project);
	}

	/**
	 * Updates an existing project.
	 *
	 * @param project the project to be updated
	 * @return the updated project
	 * @throws ResourceNotFoundException if the project does not exist
	 */
	@Override
	public Projects updateProject(Projects project) {
		if (!projectDao.existsById(project.getId())) {
			throw new ResourceNotFoundException(PROJECT_NOT_FOUND + project.getId());
		}
		return projectDao.save(project);
	}

	/**
	 * Deletes a project by its ID.
	 *
	 * @param id the ID of the project to be deleted
	 * @throws ResourceNotFoundException if the project does not exist
	 */
	@Override
	public void deleteProject(int id) {
		if (!projectDao.existsById(id)) {
			throw new ResourceNotFoundException(PROJECT_NOT_FOUND + id);
		}
		projectDao.deleteById(id);
	}

	/**
	 * Retrieves all projects.
	 *
	 * @return a list of all projects
	 */
	@Override
	public List<Projects> getAllProjects() {
		return projectDao.findAll();
	}

	/**
	 * Retrieves a project by its ID.
	 *
	 * @param id the ID of the project to be retrieved
	 * @return the project with the specified ID
	 * @throws ResourceNotFoundException if the project does not exist
	 */
	@Override
	public Projects getProjectById(int id) {
		Optional<Projects> project = projectDao.findById(id);
		if (project.isEmpty()) {
			throw new ResourceNotFoundException(PROJECT_NOT_FOUND + id);
		}
		return project.get();
	}
}