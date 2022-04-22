package io.agileintelligence.ppmtool.services;

import io.agileintelligence.ppmtool.domain.Backlog;
import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.domain.User;
import io.agileintelligence.ppmtool.exceptions.ProjectIdException;
import io.agileintelligence.ppmtool.exceptions.ProjectNotFoundException;
import io.agileintelligence.ppmtool.repository.BacklogRepository;
import io.agileintelligence.ppmtool.repository.ProjectRepository;
import io.agileintelligence.ppmtool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.util.Optional.ofNullable;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private BacklogRepository backlogRepository;
    @Autowired
    private UserRepository userRepository;

    public Project saveOrUpdateProject(Project project, String username) {
        if (project.getId() != null) {
            ofNullable(projectRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()))
                    .filter(p -> p.getProjectLeader().equals(username))
                    .orElseThrow(() -> new ProjectNotFoundException("Project not found on account " + username));
        }

        try {
            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(username);
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            if (null == project.getId()) {
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }

            if (project.getId() != null) {
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier()));
            }

            return projectRepository.save(project);
        } catch (Exception e) {
            throw new ProjectIdException("Project ID '" + project.getProjectIdentifier() + "' already exists.");
        }
    }

    public Project findByProjectIdentifier(String projectIdentifier, String username) {
        return ofNullable(projectRepository.findByProjectIdentifier(projectIdentifier))
                .filter(project -> project.getProjectLeader().equals(username))
                .orElseThrow(() -> new ProjectIdException("Project Id '" + projectIdentifier + "' does not exist."));
    }

    public Iterable<Project> findAllProject(String username) {
        return projectRepository.findByProjectLeader(username);
    }

    public void deleteProject(String projectId, String username) {
        ofNullable(findByProjectIdentifier(projectId, username))
                .ifPresent(p -> projectRepository.deleteById(p.getId()));
    }
}
