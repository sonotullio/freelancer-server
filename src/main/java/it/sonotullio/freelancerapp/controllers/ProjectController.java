package it.sonotullio.freelancerapp.controllers;


import it.sonotullio.freelancerapp.exceptions.NotFoundException;
import it.sonotullio.freelancerapp.model.Project;
import it.sonotullio.freelancerapp.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController()
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    ProjectRepository projectRepository;

    @PostMapping
    public Project create(@RequestBody Project project) {
        return projectRepository.save(project);
    }

    @GetMapping
    public Iterable<Project> findAll(Optional<String> customerId) {
        return customerId.isPresent() ? projectRepository.findAllByCustomerId(customerId.get()) : projectRepository.findAll();
    }

    @GetMapping("/{id}")
    public Project findById(@PathVariable String id) {
        return projectRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Project not found")
        );
    }

    @PutMapping("/{id}")
    public Project update(@PathVariable String id, @RequestBody Project project) {
        return projectRepository.save(project);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        projectRepository.deleteById(id);
    }
}
