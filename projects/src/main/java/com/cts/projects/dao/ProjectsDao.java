package com.cts.projects.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cts.projects.model.Projects;

@Repository
public interface ProjectsDao extends JpaRepository<Projects, Integer> {
    
}