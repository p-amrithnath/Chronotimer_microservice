package com.cts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.cts.teams.dao.TeamsDao;
import com.cts.teams.exception.ResourceNotFoundException;
import com.cts.teams.model.Teams;
import com.cts.teams.service.TeamsServiceImpl;
import com.cts.teams.utils.EmailService;


@ExtendWith(MockitoExtension.class)
class TeamsApplicationTests {

	@Mock
	private TeamsDao teamsDao;

	@Mock
	private EmailService emailService;

	@InjectMocks
	private TeamsServiceImpl teamsService;

	private Teams team;

	@BeforeEach
	public void setUp() {
		team = new Teams();
		team.setId(1);
		team.setName("John Doe");
		team.setEmail("john.doe@example.com");
		team.setEmpDesg("Developer");
		team.setRole(Teams.Role.EMPLOYEE); // Use the enum type
		team.setSalary(BigDecimal.valueOf(100000)); // Use BigDecimal for salary
		team.setDoj(LocalDate.now().minusYears(2));
	}

	@Test
	public void testGetAllTeams() {
		Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
		Page<Teams> page = new PageImpl<>(Arrays.asList(team));
		when(teamsDao.findAll(pageable)).thenReturn(page);
		Page<Teams> result = teamsService.getAllTeams(0, 10, "name");
		assertEquals(1, result.getTotalElements());
		verify(teamsDao, times(1)).findAll(pageable);
	}

	@Test
	public void testGetTeamById() {
		when(teamsDao.findById(team.getId())).thenReturn(Optional.of(team));
		Optional<Teams> foundTeam = teamsService.getTeamById(team.getId());
		assertTrue(foundTeam.isPresent());
		assertEquals(team.getId(), foundTeam.get().getId());
		verify(teamsDao, times(1)).findById(team.getId());
	}

	@Test
	public void testGetTeamById_NotFound() {
		when(teamsDao.findById(team.getId())).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> {
			teamsService.getTeamById(team.getId());
		});
	}

	@Test
	public void testCreateTeam() {
		when(teamsDao.save(team)).thenReturn(team);
		Teams createdTeam = teamsService.createTeam(team);
		assertEquals(team.getId(), createdTeam.getId());
		verify(teamsDao, times(1)).save(team);
	}

	@Test
	public void testUpdateTeam() {
		when(teamsDao.findById(team.getId())).thenReturn(Optional.of(team));
		when(teamsDao.save(team)).thenReturn(team);
		Teams updatedTeam = teamsService.updateTeam(team.getId(), team);
		assertEquals(team.getId(), updatedTeam.getId());
		verify(teamsDao, times(1)).save(team);
	}

	@Test
	public void testDeleteTeam() {
		when(teamsDao.findById(team.getId())).thenReturn(Optional.of(team));
		doNothing().when(teamsDao).delete(team);
		teamsService.deleteTeam(team.getId());
		verify(teamsDao, times(1)).delete(team);
	}

	@Test
	public void testDeleteTeam_NotFound() {
		when(teamsDao.findById(team.getId())).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> {
			teamsService.deleteTeam(team.getId());
		});
	}

}
