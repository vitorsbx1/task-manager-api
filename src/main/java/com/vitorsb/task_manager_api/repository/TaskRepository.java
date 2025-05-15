package com.vitorsb.task_manager_api.repository;

import com.vitorsb.task_manager_api.model.Task;
import com.vitorsb.task_manager_api.model.enums.TaskPriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("""
            SELECT t
              FROM Task t
             WHERE (:description IS NULL OR UPPER(t.description) LIKE CONCAT('%', UPPER(:description), '%'))
               AND (:priority IS NULL OR t.priority = :priority)
               AND (:startDate IS NULL OR :endDate IS NULL OR t.createdAt BETWEEN :startDate and :endDate)
            """)
    List<Task> findTask(@Param("description") String description,
                        @Param("priority") TaskPriority priority,
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);
}
