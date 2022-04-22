package io.agileintelligence.ppmtool.web;

import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.services.MapValidationErrorService;
import io.agileintelligence.ppmtool.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping("")
    public ResponseEntity<?> postProject(@Valid @RequestBody Project project, BindingResult result, Principal principal) {

        ResponseEntity<?> errorMap = mapValidationErrorService.validate(result);
        if (errorMap != null) return errorMap;

        Project projectSaved = projectService.saveOrUpdateProject(project, principal.getName());
        return new ResponseEntity(project, HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProject(@PathVariable String projectId, Principal principal) {
        Project project = projectService.findByProjectIdentifier(projectId, principal.getName());
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @GetMapping("/all")
    public Iterable<Project> getProjects(Principal principal) {
        return projectService.findAllProject(principal.getName());
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> getProjects(@PathVariable String projectId, Principal principal) {
        projectService.deleteProject(projectId, principal.getName());
        return new ResponseEntity<String>("Project with ID '" + projectId + "' was deleted", HttpStatus.OK);
    }

}
