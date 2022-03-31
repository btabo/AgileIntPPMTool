package io.agileintelligence.ppmtool.web;

import io.agileintelligence.ppmtool.domain.ProjectTask;
import io.agileintelligence.ppmtool.services.MapValidationErrorService;
import io.agileintelligence.ppmtool.services.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

    @Autowired
    private ProjectTaskService projectTaskService;
    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping("/{backlogId}")
    public ResponseEntity<?> postProjectTask(@Valid @RequestBody ProjectTask projectTask,
                                             BindingResult result, @PathVariable String backlogId) {
        ResponseEntity<?> errorMap = mapValidationErrorService.validate(result);
        if (errorMap != null) return errorMap;

        ProjectTask projectTaskSaved = projectTaskService.addProjectTask(backlogId, projectTask);
        return new ResponseEntity(projectTaskSaved, HttpStatus.CREATED);
    }

    @GetMapping("/{backlogId}")
    public ResponseEntity<Iterable<ProjectTask>> getProjectBacklog(@PathVariable String backlogId) {
        return new ResponseEntity<>(projectTaskService.findBacklogById(backlogId), HttpStatus.OK);
    }

    @GetMapping("/{backlogId}/{ptId}")
    public ResponseEntity<ProjectTask> getProjectTask(@PathVariable String backlogId, @PathVariable String ptId) {
        return new ResponseEntity<>(projectTaskService.findPTByProjectSequence(backlogId, ptId), HttpStatus.OK);
    }

    @PatchMapping("/{backlogId}/{ptId}")
    public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask, BindingResult result,
                                                         @PathVariable String backlogId, @PathVariable String ptId) {
        ResponseEntity<?> errorMap = mapValidationErrorService.validate(result);
        if (errorMap != null) return errorMap;

        ProjectTask updatedProjectTask = projectTaskService.updateByProjectSequence(projectTask, backlogId, ptId);
        return new ResponseEntity(updatedProjectTask, HttpStatus.OK);
    }

}
