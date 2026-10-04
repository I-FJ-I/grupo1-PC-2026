package com.example.taskapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.taskapi.exception.ResourceNotFoundException;
import com.example.taskapi.model.Task;
import com.example.taskapi.model.TaskPriority;
import com.example.taskapi.model.TaskStatus;
import com.example.taskapi.repository.TaskRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

  @Mock private TaskRepository taskRepository;

  @InjectMocks private TaskService taskService;

  private Task validTask;

  @BeforeEach
  void setUp() {
    validTask =
        new Task(
            "Aprender Java 21",
            "Estudiar virtual threads",
            TaskStatus.PENDING,
            TaskPriority.HIGH,
            LocalDate.now().plusDays(5));
    validTask.setId(1L);
  }

  @Test
  void createTask_ShouldReturnSavedTask_WhenDataIsValid() {
    when(taskRepository.save(any(Task.class))).thenReturn(validTask);

    Task createdTask = taskService.createTask(validTask);

    assertNotNull(createdTask);
    assertEquals("Aprender Java 21", createdTask.getTitle());
    verify(taskRepository, times(1)).save(validTask);
  }

  @Test
  void createTask_ShouldThrowException_WhenDueDateIsInThePast() {
    Task pastTask =
        new Task(
            "Tarea vieja",
            "Desc",
            TaskStatus.PENDING,
            TaskPriority.LOW,
            LocalDate.now().minusDays(1));

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              taskService.createTask(pastTask);
            });

    assertEquals("La fecha límite no puede estar en el pasado.", exception.getMessage());
    verify(taskRepository, never()).save(any(Task.class));
  }

  @Test
  void updateTask_ShouldThrowResourceNotFound_WhenTaskDoesNotExist() {
    when(taskRepository.findById(99L)).thenReturn(Optional.empty());

    ResourceNotFoundException exception =
        assertThrows(
            ResourceNotFoundException.class,
            () -> {
              taskService.updateTask(99L, validTask);
            });

    assertTrue(exception.getMessage().contains("no encontrada"));
    verify(taskRepository, never()).save(any(Task.class));
  }

  @Test
  void updateTask_ShouldThrowException_WhenTitleIsBlank() {
    when(taskRepository.findById(1L)).thenReturn(Optional.of(validTask));

    Task invalidUpdate =
        new Task("   ", "Desc", TaskStatus.PENDING, TaskPriority.LOW, LocalDate.now().plusDays(1));

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              taskService.updateTask(1L, invalidUpdate);
            });

    assertEquals("El título no puede estar en blanco.", exception.getMessage());
    verify(taskRepository, never()).save(any(Task.class));
  }

  @Test
  void deleteTask_ShouldCallDelete_WhenTaskExists() {
    when(taskRepository.findById(1L)).thenReturn(Optional.of(validTask));

    taskService.deleteTask(1L);

    verify(taskRepository, times(1)).findById(1L);
    verify(taskRepository, times(1)).delete(validTask);
  }

  @Test
  void deleteTask_ShouldThrowResourceNotFound_WhenTaskDoesNotExist() {
    when(taskRepository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> taskService.deleteTask(99L));
    verify(taskRepository, never()).delete(any(Task.class));
  }

  @Test
  void getTaskStatistics_ShouldReturnCorrectCounts() {
    when(taskRepository.count()).thenReturn(10L);
    when(taskRepository.countByStatus(TaskStatus.COMPLETED)).thenReturn(4L);
    when(taskRepository.countByStatus(TaskStatus.PENDING)).thenReturn(3L);
    when(taskRepository.countByStatus(TaskStatus.IN_PROGRESS)).thenReturn(3L);
    when(taskRepository.countByDueDateBeforeAndStatusNot(any(), eq(TaskStatus.COMPLETED)))
        .thenReturn(2L);

    var stats = taskService.getTaskStatistics();

    assertEquals(10L, stats.totalTasks());
    assertEquals(4L, stats.completedTasks());
    assertEquals(6L, stats.pendingTasks());
    assertEquals(2L, stats.overdueTasks());
  }
}
