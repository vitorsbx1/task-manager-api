package com.vitorsb.task_manager_api.controller;

import com.vitorsb.task_manager_api.dto.request.TaskRequest;
import com.vitorsb.task_manager_api.dto.request.TaskUpdateRequest;
import com.vitorsb.task_manager_api.dto.response.TaskResponse;
import com.vitorsb.task_manager_api.model.enums.TaskPriority;
import com.vitorsb.task_manager_api.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest taskRequest){
        TaskResponse created = taskService.createTask(taskRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping()
    public ResponseEntity<List<TaskResponse>> getTasks(
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "priority", required = false) TaskPriority priority,
            @RequestParam(value = "createdAt", required = false) LocalDate createdAt) {
        return ResponseEntity.ok(taskService.getTasks(description, priority, createdAt));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable Long id){
        return ResponseEntity.ok(taskService.getTask(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id,
                                                   @RequestBody(required = false) TaskUpdateRequest taskUpdateRequest
                                                   ){
        return ResponseEntity.ok(taskService.updateTask(id,taskUpdateRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

}
