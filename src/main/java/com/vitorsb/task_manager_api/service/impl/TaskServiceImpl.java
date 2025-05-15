package com.vitorsb.task_manager_api.service.impl;

import com.vitorsb.task_manager_api.converter.TaskConverter;
import com.vitorsb.task_manager_api.dto.request.TaskRequest;
import com.vitorsb.task_manager_api.dto.request.TaskUpdateRequest;
import com.vitorsb.task_manager_api.dto.response.TaskResponse;
import com.vitorsb.task_manager_api.model.Task;
import com.vitorsb.task_manager_api.model.enums.TaskPriority;
import com.vitorsb.task_manager_api.model.enums.TaskStatus;
import com.vitorsb.task_manager_api.repository.TaskRepository;
import com.vitorsb.task_manager_api.service.TaskService;
import com.vitorsb.task_manager_api.utils.DateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    @Override
    public TaskResponse createTask(TaskRequest taskRequest) {
        return TaskConverter.fromEntity(taskRepository.save(TaskConverter.fromDto(taskRequest)));
    }

    @Override
    public List<TaskResponse> getTasks(String description, TaskPriority priority, LocalDate createdAt) {

        LocalDateTime start = null;
        LocalDateTime end = null;

        if(createdAt != null) {
            start = DateUtils.toStartOfDay(createdAt);
            end = DateUtils.toEndOfDay(createdAt);
        }

        return taskRepository.findTask(description, priority, start, end)
                .stream()
                .map(TaskConverter::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public TaskResponse getTask(Long id) {
       return taskRepository.findById(id).map(TaskConverter::fromEntity).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Task not found"));
    }

    @Override
    public TaskResponse updateTask(Long id, TaskUpdateRequest taskUpdateRequest) {
        return taskRepository.findById(id).map(task -> {
            applyUpdates(task, taskUpdateRequest);
            return TaskConverter.fromEntity(taskRepository.save(task));
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    public void applyUpdates(Task task, TaskUpdateRequest taskUpdateRequest){
        if(taskUpdateRequest != null){
            if(taskUpdateRequest.description() != null && !taskUpdateRequest.description().isBlank()){
                task.setDescription(taskUpdateRequest.description());

            }
            if(taskUpdateRequest.priority() != null){
                task.setPriority(taskUpdateRequest.priority());
            }
            if(taskUpdateRequest.status() != null){
                task.setStatus(taskUpdateRequest.status());
                if(taskUpdateRequest.status() == TaskStatus.COMPLETED){
                    task.setCompletedAt(LocalDateTime.now());
                }
            }
        }
        task.setEditedAt(LocalDateTime.now());
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.findById(id).map(task -> {
            task.setExcludedAt(LocalDateTime.now());
            return taskRepository.save(task);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }


}
