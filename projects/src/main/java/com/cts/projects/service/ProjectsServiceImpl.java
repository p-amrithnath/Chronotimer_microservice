package com.cts.projects.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.projects.dao.ProjectsDao;
import com.cts.projects.exception.ResourceNotFoundException;
import com.cts.projects.model.Projects;

@Service
public class ProjectsServiceImpl implements ProjectsService {

	@Autowired
	private ProjectsDao projectDao;

	@Override
	public Projects saveProject(Projects project) {
		return projectDao.save(project);
	}

	@Override
	public Projects updateProject(Projects project) {
		if (!projectDao.existsById(project.getId())) {
			throw new ResourceNotFoundException("Project not found with id: " + project.getId());
		}
		return projectDao.save(project);
	}

	@Override
	public void deleteProject(int id) {
		if (!projectDao.existsById(id)) {
			throw new ResourceNotFoundException("Project not found with id: " + id);
		}
		projectDao.deleteById(id);
	}

	@Override
	public List<Projects> getAllProjects() {
		return projectDao.findAll();
	}

	@Override
	public Projects getProjectById(int id) {
		Optional<Projects> project = projectDao.findById(id);
		if (project.isEmpty()) {
			throw new ResourceNotFoundException("Project not found with id: " + id);
		}
		return project.get();
	}
}