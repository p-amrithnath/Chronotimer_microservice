package com.cts.teams.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.cts.teams.dao.TeamsDao;
import com.cts.teams.exception.ResourceNotFoundException;
import com.cts.teams.model.Teams;

@Service
public class TeamsServiceImpl implements TeamsService {

	private final TeamsDao teamsDao;

	private static final String TEAM_NOT_FOUND = "team not found with id:";

	@Autowired
	public TeamsServiceImpl(TeamsDao teamsDao) {
		this.teamsDao = teamsDao;
	}

//  @Autowired
//	private EmailService emailService;

	/**
	 * Retrieves a paginated list of all teams, sorted by the specified attribute.
	 * 
	 * @param page   the page number to retrieve
	 * @param size   the number of records per page
	 * @param sortBy the attribute to sort by
	 * @return a paginated list of teams
	 */

	@Override
	public Page<Teams> getAllTeams(int page, int size, String sortBy) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
		return teamsDao.findAll(pageable);
	}

	/**
	 * Retrieves a team by its ID.
	 * 
	 * @param id the ID of the team to retrieve
	 * @return an Optional containing the team if found, or throws
	 *         ResourceNotFoundException if not found
	 */

	@Override
	public Optional<Teams> getTeamById(int id) {
		Optional<Teams> optional = teamsDao.findById(id);
		if (optional.isPresent())
			return Optional.ofNullable(optional.get());
		else
			throw new ResourceNotFoundException(TEAM_NOT_FOUND + id);
	}

	/**
	 * Creates a new team.
	 * 
	 * @param team the team to create
	 * @return the created team
	 */

	@Override
	public Teams createTeam(Teams team) {
//		String Text = "User email" + team.getEmail();
//		emailService.sendEmail(team.getEmail(), "Welcome to CTS ",Text);
		return teamsDao.save(team);
	}

	/**
	 * Updates an existing team by its ID.
	 * 
	 * @param id          the ID of the team to update
	 * @param teamDetails the new details of the team
	 * @return the updated team
	 */

	@Override
	public Teams updateTeam(int id, Teams teamDetails) {
		Teams team = teamsDao.findById(id).orElseThrow(() -> new ResourceNotFoundException(TEAM_NOT_FOUND + id));
		team.setName(teamDetails.getName());
		team.setEmail(teamDetails.getEmail());
		team.setEmpDesg(teamDetails.getEmpDesg());
		team.setRole(teamDetails.getRole());
		team.setSalary(teamDetails.getSalary());
		team.setDoj(teamDetails.getDoj());
		return teamsDao.save(team);
	}

	/**
	 * Deletes a team by its ID.
	 * 
	 * @param id the ID of the team to delete
	 */

	@Override
	public void deleteTeam(int id) {
		Teams team = teamsDao.findById(id).orElseThrow(() -> new ResourceNotFoundException(TEAM_NOT_FOUND + id));
		teamsDao.delete(team);
	}

}