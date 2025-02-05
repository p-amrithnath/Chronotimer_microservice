package com.cts.projects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cts.projects.dao.ProjectsDao;
import com.cts.projects.exception.ResourceNotFoundException;
import com.cts.projects.model.Projects;
import com.cts.projects.service.ProjectsServiceImpl;


@ExtendWith(MockitoExtension.class)
class ProjectsApplicationTests {

	@Mock
	private ProjectsDao projectDao;

	@InjectMocks
	private ProjectsServiceImpl projectsService;

	private Projects project;

	@BeforeEach
	public void setUp() {
		project = new Projects();
		project.setId(1);
		project.setProjName("Test Project");
		project.setType("Development");
		project.setStartDate(LocalDate.now().minusDays(1));
		project.setCloseDate(LocalDate.now().plusDays(10));
		project.setTam("John Doe");
		project.setEstimatedhrs(100);
		project.setDescription("Test project description");
	}

	@Test
	public void testSaveProject() {
		when(projectDao.save(project)).thenReturn(project);
		Projects savedProject = projectsService.saveProject(project);
		assertEquals(project.getId(), savedProject.getId());
		verify(projectDao, times(1)).save(project);
	}

	@Test
	public void testUpdateProject() {
		when(projectDao.existsById(project.getId())).thenReturn(true);
		when(projectDao.save(project)).thenReturn(project);
		Projects updatedProject = projectsService.updateProject(project);
		assertEquals(project.getId(), updatedProject.getId());
		verify(projectDao, times(1)).save(project);
	}

	@Test
	public void testDeleteProject() {
		when(projectDao.existsById(project.getId())).thenReturn(true);
		projectsService.deleteProject(project.getId());
		verify(projectDao, times(1)).deleteById(project.getId());
	}

	@Test
	public void testGetAllProjects() {
		when(projectDao.findAll()).thenReturn(Arrays.asList(project));
		assertFalse(projectsService.getAllProjects().isEmpty());
		verify(projectDao, times(1)).findAll();
	}

	@Test
	public void testGetProjectById() {
		when(projectDao.findById(project.getId())).thenReturn(Optional.of(project));
		Projects foundProject = projectsService.getProjectById(project.getId());
		assertEquals(project.getId(), foundProject.getId());
		verify(projectDao, times(1)).findById(project.getId());
	}

	@Test
	public void testGetProjectById_NotFound() {
		when(projectDao.findById(project.getId())).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> {
			projectsService.getProjectById(project.getId());
		});
	}
}
