package com.vitorsb.task_manager_api.service.impl;

import com.vitorsb.task_manager_api.converter.TaskConverter;
import com.vitorsb.task_manager_api.dto.request.TaskRequest;
import com.vitorsb.task_manager_api.dto.request.TaskUpdateRequest;
import com.vitorsb.task_manager_api.dto.response.TaskResponse;
import com.vitorsb.task_manager_api.model.Task;
import com.vitorsb.task_manager_api.model.enums.TaskPriority;
import com.vitorsb.task_manager_api.model.enums.TaskStatus;
import com.vitorsb.task_manager_api.repository.TaskRepository;
import com.vitorsb.task_manager_api.utils.DateUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class )
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;


    @Test
    void shouldCreateTaskSuccessfully(){

        TaskRequest request = TaskRequest.builder()
                .description("TESTE")
                .taskPriority(TaskPriority.LOW)
                .build();

        Task mockEntity = Task.builder()
                .id(1L)
                .description("TESTE")
                .priority(TaskPriority.LOW)
                .status(TaskStatus.PENDING)
                .build();

        when(taskRepository.save(any(Task.class))).thenReturn(mockEntity);

        TaskResponse response = taskService.createTask(request);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.description()).isEqualTo("TESTE");
        assertThat(response.priority()).isEqualTo(TaskPriority.LOW.name());

        verify(taskRepository).save(any(Task.class));


    }

    @Test
    void shouldFindListTaskSuccessfully(){
        String description = "TESTE";
        TaskPriority priority = TaskPriority.LOW;
        LocalDate createdAt = LocalDate.now();

        LocalDateTime startDate = DateUtils.toStartOfDay(createdAt);
        LocalDateTime endDate = DateUtils.toEndOfDay(createdAt);


        Task task = Task
                .builder()
                .id(1L)
                .description("TESTE")
                .priority(TaskPriority.LOW)
                .createdAt(LocalDateTime.now())
                .build();

        List<Task> tasks = List.of(task);

        when(taskRepository.findTask(any(String.class), any(TaskPriority.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(tasks);

        List<TaskResponse> tasksResponse = taskService.getTasks(description, priority, createdAt);

        for(TaskResponse taskResponse : tasksResponse){
            assertThat(taskResponse.id()).isNotNull();
            assertThat(taskResponse.description()).isNotNull();
            assertThat(taskResponse.priority()).isNotNull();
            assertThat(taskResponse.createdAt()).isNotNull();
        }

        verify(taskRepository).findTask(any(), any(), any(), any());
    }

    @Test
    void shouldFindListTaskSuccessfullyWhenCreatedAtIsNull(){
        String description = "TESTE";
        TaskPriority priority = TaskPriority.LOW;
        LocalDate createdAt = null;

        Task task = Task
                .builder()
                .id(1L)
                .description("TESTE")
                .priority(TaskPriority.LOW)
                .createdAt(LocalDateTime.now())
                .build();

        List<Task> tasks = List.of(task);

        when(taskRepository.findTask(any(String.class), any(TaskPriority.class), isNull(), isNull())).thenReturn(tasks);

        List<TaskResponse> tasksResponse = taskService.getTasks(description, priority, createdAt);

        for(TaskResponse taskResponse : tasksResponse){
            assertThat(taskResponse.id()).isNotNull();
            assertThat(taskResponse.description()).isNotNull();
            assertThat(taskResponse.priority()).isNotNull();
            assertThat(taskResponse.createdAt()).isNotNull();
        }

        verify(taskRepository).findTask(any(), any(), isNull(), isNull());
    }

    @Test
    void shoudFindTaskSucessfully(){
        Long id = 1L;

        Task task = Task
                .builder()
                .id(1L)
                .priority(TaskPriority.LOW)
                .build();

        when(taskRepository.findById(any())).thenReturn(Optional.ofNullable(task));

        TaskResponse response = taskService.getTask(id);

        assertThat(response.id()).isNotNull();

        verify(taskRepository).findById(any());
    }


    @Test
    void shouldThrowExceptionWhenTaskNotFound(){
        Long id = 1L;

        when(taskRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTask(id))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Task not found");
    }


    @Test
    void shouldUpdateTaskSucessfully(){
        Long id = 1L;

        TaskUpdateRequest taskUpdateRequest = TaskUpdateRequest.builder()
                .description("TESTE1")
                .status(TaskStatus.COMPLETED)
                .priority(TaskPriority.LOW)
                .build();

        Task task = Task
                .builder()
                .id(1L)
                .description("TESTE")
                .priority(TaskPriority.LOW)
                .build();


        when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponse response = taskService.updateTask(id, taskUpdateRequest);

        assertThat(response.id()).isEqualTo(id);
        assertThat(task.getDescription()).isEqualTo("TESTE1");
        assertThat(task.getPriority()).isEqualTo(TaskPriority.LOW);
        assertThat(task.getStatus()).isEqualTo(TaskStatus.COMPLETED);
        assertThat(task.getCompletedAt()).isNotNull();

        verify(taskRepository).findById(id);
        verify(taskRepository).save(task);


    }

    @Test
    void shouldUpdateDateExcludeSucessufully(){
        Long id = 1L;


        Task task = Task
                .builder()
                .id(1L)
                .description("TESTE")
                .priority(TaskPriority.LOW)
                .build();


        when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        taskService.deleteTask(id);

        assertThat(task.getExcludedAt()).isNotNull();
        verify(taskRepository).findById(id);
        verify(taskRepository).save(task);
    }



}