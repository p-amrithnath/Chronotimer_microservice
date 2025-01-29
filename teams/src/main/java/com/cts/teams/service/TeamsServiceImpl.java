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
import com.cts.teams.utils.EmailService;

@Service
public class TeamsServiceImpl implements TeamsService {

	@Autowired
	private TeamsDao teamsDao;
	
    @Autowired
	private EmailService emailService;

	@Override
	public Page<Teams> getAllTeams(int page, int size, String sortBy) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
		return teamsDao.findAll(pageable);
	}

	@Override
	public Optional<Teams> getTeamById(int id) {
		Optional<Teams> optional= teamsDao.findById(id);
		if(optional.isPresent())
		     return Optional.ofNullable(optional.get());
		else
			throw new ResourceNotFoundException("team not found with id: " + id);
	}

	@Override
	public Teams createTeam(Teams team) {
		String Text = "User email" + team.getEmail();
//		emailService.sendEmail(team.getEmail(), "Welcome to CTS ",Text);
		return teamsDao.save(team);
	}

	@Override
	public Teams updateTeam(int id, Teams teamDetails) {
		Teams team = teamsDao.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("team not found with id: " + id));
		team.setName(teamDetails.getName());
		team.setEmail(teamDetails.getEmail());
		team.setEmpDesg(teamDetails.getEmpDesg());
		team.setRole(teamDetails.getRole());
		team.setSalary(teamDetails.getSalary());
		team.setDoj(teamDetails.getDoj());
		return teamsDao.save(team);
	}

	@Override
	public void deleteTeam(int id) {
		Teams team = teamsDao.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("team not found with id: " + id));
		teamsDao.delete(team);
	}

}