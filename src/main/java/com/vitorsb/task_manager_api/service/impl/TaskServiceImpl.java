package com.vitorsb.task_manager_api.service.impl;

import com.vitorsb.task_manager_api.converter.TaskConverter;
import com.vitorsb.task_manager_api.dto.request.TaskRequest;
import com.vitorsb.task_manager_api.dto.response.TaskResponse;
import com.vitorsb.task_manager_api.repository.TaskRepository;
import com.vitorsb.task_manager_api.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    public List<TaskResponse> getTasks() {
        return taskRepository.findAll()
                             .stream()
                             .map(TaskConverter::fromEntity)
                             .collect(Collectors.toList());
    }

    @Override
    public TaskResponse getTask(Long id) {
       return taskRepository.findById(id).map(TaskConverter::fromEntity).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Task not found"));
    }
}
