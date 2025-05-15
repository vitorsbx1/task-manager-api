package com.vitorsb.task_manager_api.dto.request;

import com.vitorsb.task_manager_api.model.enums.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record TaskRequest(@NotBlank String description,
                          @NotNull TaskPriority taskPriority) {
}
