package com.vitorsb.task_manager_api.repository;

import com.vitorsb.task_manager_api.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
