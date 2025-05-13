package com.vitorsb.task_manager_api.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskResponse(Long id, String description, String priority, LocalDateTime createdAt) {
}
