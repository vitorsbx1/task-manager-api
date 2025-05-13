package com.vitorsb.task_manager_api.converter;

import com.vitorsb.task_manager_api.dto.request.TaskRequest;
import com.vitorsb.task_manager_api.dto.response.TaskResponse;
import com.vitorsb.task_manager_api.model.Task;
import com.vitorsb.task_manager_api.model.enums.TaskStatus;

import java.time.LocalDateTime;

public class TaskConverter {

    public static Task fromDto(TaskRequest taskRequest){
        return Task.builder()
                .description(taskRequest.description())
                .priority(taskRequest.taskPriority())
                .createdAt(LocalDateTime.now())
                .status(TaskStatus.PENDING)
                .build();
    }

    public static TaskResponse fromEntity(Task task){
        return TaskResponse.builder()
                .id(task.getId())
                .description(task.getDescription())
                .priority(task.getPriority().name())
                .createdAt(task.getCreatedAt())
                .build();
    }
}
