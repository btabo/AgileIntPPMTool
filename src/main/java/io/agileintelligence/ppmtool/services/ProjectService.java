package io.agileintelligence.ppmtool.services;

import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.exceptions.ProjectIdException;
import io.agileintelligence.ppmtool.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.util.Optional.ofNullable;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public Project saveOrUpdateProject(Project project) {
        try {

            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            return projectRepository.save(project);
        } catch (Exception e) {
            throw new ProjectIdException("Project ID '" + project.getProjectIdentifier() + "' already exists.");
        }
    }

    public Project findByProjectIdentifier(String projectIdentifier) {
        return ofNullable(projectRepository.findByProjectIdentifier(projectIdentifier))
                .orElseThrow(() -> new ProjectIdException("Project Id '" + projectIdentifier + "' does not exist."));
    }

    public Iterable<Project> findAllProject() {
        return projectRepository.findAll();
    }

    public void deleteProject(String projectId) {
        ofNullable(projectRepository.findByProjectIdentifier(projectId))
                .ifPresent(p -> projectRepository.deleteById(p.getId()));
    }
}
