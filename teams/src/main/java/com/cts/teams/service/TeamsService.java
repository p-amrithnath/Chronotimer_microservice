package com.cts.teams.service;

import org.springframework.data.domain.Page;
import com.cts.teams.model.Teams;

import java.util.Optional;

public interface TeamsService {

    Page<Teams> getAllTeams(int page, int size, String sortBy);

    Optional<Teams> getTeamById(int id);

    Teams createTeam(Teams employee);

    Teams updateTeam(int id, Teams employeeDetails);

    void deleteTeam(int id);
}