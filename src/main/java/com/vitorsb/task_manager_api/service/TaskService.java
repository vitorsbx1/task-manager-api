package com.vitorsb.task_manager_api.service;

import com.vitorsb.task_manager_api.dto.request.TaskRequest;
import com.vitorsb.task_manager_api.dto.request.TaskUpdateRequest;
import com.vitorsb.task_manager_api.dto.response.TaskResponse;
import com.vitorsb.task_manager_api.model.enums.TaskPriority;
import com.vitorsb.task_manager_api.model.enums.TaskStatus;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {

    TaskResponse createTask(TaskRequest taskRequest);

    List<TaskResponse> getTasks(String description, TaskPriority priority, LocalDate createdAt);

    TaskResponse getTask(Long id);

    TaskResponse updateTask(Long id, TaskUpdateRequest taskUpdateRequest);

    void deleteTask(Long id);

}
