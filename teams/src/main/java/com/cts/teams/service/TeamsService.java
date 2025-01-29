package com.cts.teams.service;

import org.springframework.data.domain.Page;
import com.cts.teams.model.Teams;

import java.util.Optional;

public interface TeamsService {

    public abstract Page<Teams> getAllTeams(int page, int size, String sortBy);

    public abstract Optional<Teams> getTeamById(int id);

    public abstract Teams createTeam(Teams employee);

    public abstract Teams updateTeam(int id, Teams employeeDetails);

    public void deleteTeam(int id);
}