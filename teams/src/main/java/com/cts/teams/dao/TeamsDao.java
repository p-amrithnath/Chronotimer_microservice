package com.cts.teams.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cts.teams.model.Teams;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface TeamsDao extends JpaRepository<Teams, Integer>, JpaSpecificationExecutor<Teams> {
}