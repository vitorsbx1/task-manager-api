package com.vitorsb.task_manager_api.dto.request;

import com.vitorsb.task_manager_api.model.enums.TaskPriority;
import com.vitorsb.task_manager_api.model.enums.TaskStatus;
import lombok.Builder;

@Builder
public record TaskUpdateRequest(String description, TaskPriority priority, TaskStatus status) {
}
