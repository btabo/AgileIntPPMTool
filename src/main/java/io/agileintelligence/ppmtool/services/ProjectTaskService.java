package io.agileintelligence.ppmtool.services;

import io.agileintelligence.ppmtool.domain.Backlog;
import io.agileintelligence.ppmtool.domain.ProjectTask;
import io.agileintelligence.ppmtool.exceptions.ProjectNotFoundException;
import io.agileintelligence.ppmtool.repository.BacklogRepository;
import io.agileintelligence.ppmtool.repository.ProjectRepository;
import io.agileintelligence.ppmtool.repository.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.util.Optional.ofNullable;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {

        Backlog backlog = ofNullable(backlogRepository.findByProjectIdentifier(projectIdentifier))
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        projectTask.setBacklog(backlog);
        projectTask.setProjectIdentifier(projectIdentifier);

        Integer backlogSequence = backlog.getPTSequence();
        backlogSequence++;

        backlog.setPTSequence(backlogSequence);
        backlogRepository.save(backlog);
        projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence);

        if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
            projectTask.setPriority(3);
        }
        if (projectTask.getStatus() == null || projectTask.getStatus() == "") {
            projectTask.setStatus("TO_DO");
        }
        return projectTaskRepository.save(projectTask);
    }

    public Iterable<ProjectTask> findBacklogById(String backlogId) {
        ofNullable(projectRepository.findByProjectIdentifier(backlogId))
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID = '" + backlogId + "' not found"));
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlogId);
    }

    public ProjectTask findPTByProjectSequence(String backlogId, String projectSequence) {
        Backlog backlog = ofNullable(backlogRepository.findByProjectIdentifier(backlogId))
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID = '" + backlogId + "' not found"));

        ProjectTask projectTask = ofNullable(projectTaskRepository.findByProjectSequence(projectSequence))
                .orElseThrow(() -> new ProjectNotFoundException("Project Task '" + projectSequence + "' not found"));

        if (!projectTask.getProjectIdentifier().equals(backlogId)) {
            throw new ProjectNotFoundException("Project Task '" + projectSequence + "' does not exist in project '" + backlogId + "'");
        }

        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedProjectTask, String backlogId, String projectSequence) {
        Backlog backlog = ofNullable(backlogRepository.findByProjectIdentifier(backlogId))
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID = '" + backlogId + "' not found"));

        ProjectTask projectTask = ofNullable(projectTaskRepository.findByProjectSequence(updatedProjectTask.getProjectSequence()))
                .orElseThrow(() -> new ProjectNotFoundException("Project Task '" + projectSequence + "' not found"));

        projectTaskRepository.save(updatedProjectTask);

        if (!projectTask.getProjectIdentifier().equals(backlogId)) {
            throw new ProjectNotFoundException("Project Task '" + projectSequence + "' does not exist in project '" + backlogId + "'");
        }

        return projectTask;
    }
}
