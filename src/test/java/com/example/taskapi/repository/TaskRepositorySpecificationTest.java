package com.example.taskapi.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.taskapi.model.Task;
import com.example.taskapi.model.TaskPriority;
import com.example.taskapi.model.TaskStatus;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class TaskRepositorySpecificationTest {

  @Autowired private TaskRepository taskRepository;

  @BeforeEach
  void setUp() {
    taskRepository.save(
        new Task(
            "Aprender Java",
            "Estudiar virtual threads",
            TaskStatus.PENDING,
            TaskPriority.HIGH,
            LocalDate.now().plusDays(5)));
    taskRepository.save(
        new Task(
            "Escribir tests",
            "Cubrir el servicio",
            TaskStatus.IN_PROGRESS,
            TaskPriority.MEDIUM,
            LocalDate.now().plusDays(10)));
    taskRepository.save(
        new Task(
            "Revisar review",
            "Aprobación del PR",
            TaskStatus.COMPLETED,
            TaskPriority.LOW,
            LocalDate.now().plusDays(20)));
  }

  @Test
  void findAll_shouldReturnAll_WhenNoFiltersProvided() {
    List<Task> result = taskRepository.findAll(TaskSpecification.filterBy(null, null, null, null));

    assertEquals(3, result.size());
  }

  @Test
  void findAll_shouldFilterByStatus() {
    List<Task> result =
        taskRepository.findAll(TaskSpecification.filterBy(TaskStatus.PENDING, null, null, null));

    assertEquals(1, result.size());
    assertEquals(TaskStatus.PENDING, result.get(0).getStatus());
  }

  @Test
  void findAll_shouldFilterByPriority() {
    List<Task> result =
        taskRepository.findAll(TaskSpecification.filterBy(null, TaskPriority.HIGH, null, null));

    assertEquals(1, result.size());
    assertEquals(TaskPriority.HIGH, result.get(0).getPriority());
  }

  @Test
  void findAll_shouldFilterByDueDateLessThanOrEqualTo() {
    List<Task> result =
        taskRepository.findAll(
            TaskSpecification.filterBy(null, null, LocalDate.now().plusDays(10), null));

    assertEquals(2, result.size());
  }

  @Test
  void findAll_shouldFilterByTitleCaseInsensitive() {
    List<Task> result =
        taskRepository.findAll(TaskSpecification.filterBy(null, null, null, "java"));

    assertEquals(1, result.size());
    assertEquals("Aprender Java", result.get(0).getTitle());
  }

  @Test
  void findAll_shouldCombineFilters() {
    List<Task> result =
        taskRepository.findAll(
            TaskSpecification.filterBy(TaskStatus.IN_PROGRESS, TaskPriority.MEDIUM, null, "tests"));

    assertEquals(1, result.size());
    assertEquals("Escribir tests", result.get(0).getTitle());
  }
}
