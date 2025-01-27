package com.cts.projects.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cts.projects.model.Projects;
import com.cts.projects.service.ProjectsService;

@RestController
@RequestMapping("/projects")
public class ProjectsController {

    @Autowired
    private ProjectsService projectService;

    @PostMapping("/save")
    public ResponseEntity<Projects> saveProject(@RequestBody Projects project) {
        Projects savedProject = projectService.saveProject(project);
        return ResponseEntity.ok(savedProject);
    }

    @PutMapping("/edit")
    public ResponseEntity<Projects> updateProject(@RequestBody Projects project) {
        Projects updatedProject = projectService.updateProject(project);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable int id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Projects>> getAllProjects() {
        List<Projects> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Projects> getProjectById(@PathVariable int id) {
        Projects project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }
}