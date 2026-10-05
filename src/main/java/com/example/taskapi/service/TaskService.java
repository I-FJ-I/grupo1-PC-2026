package com.example.taskapi.service;

import com.example.taskapi.dto.TaskStats;
import com.example.taskapi.exception.ResourceNotFoundException;
import com.example.taskapi.model.Task;
import com.example.taskapi.model.TaskPriority;
import com.example.taskapi.model.TaskStatus;
import com.example.taskapi.repository.TaskRepository;
import com.example.taskapi.repository.TaskSpecification;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

  private final TaskRepository taskRepository;

  public TaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  public List<Task> searchTasks(
      TaskStatus status, TaskPriority priority, LocalDate dueDate, String title) {
    return taskRepository.findAll(TaskSpecification.filterBy(status, priority, dueDate, title));
  }

  public Page<Task> getAllTasks(Pageable pageable) {
    return taskRepository.findAll(pageable);
  }

  public Task getTaskById(Long id) {
    return taskRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con el id: " + id));
  }

  public Task createTask(Task task) {
    validateBusinessRules(task);
    return taskRepository.save(task);
  }

  public Task updateTask(Long id, Task taskDetails) {
    Task existingTask = getTaskById(id);

    validateBusinessRules(taskDetails);

    existingTask.setTitle(taskDetails.getTitle());
    existingTask.setDescription(taskDetails.getDescription());
    existingTask.setStatus(taskDetails.getStatus());
    existingTask.setPriority(taskDetails.getPriority());
    existingTask.setDueDate(taskDetails.getDueDate());

    return taskRepository.save(existingTask);
  }

  public Task updateTaskStatus(Long id, TaskStatus status) {
    Task existingTask = getTaskById(id);
    existingTask.setStatus(status);
    return taskRepository.save(existingTask);
  }

  public void deleteTask(Long id) {
    Task task = getTaskById(id);
    taskRepository.delete(task);
  }

  private void validateBusinessRules(Task task) {
    if (task.getDueDate() != null && task.getDueDate().isBefore(LocalDate.now())) {
      throw new IllegalArgumentException("La fecha límite no puede estar en el pasado.");
    }
    if (task.getTitle() != null && task.getTitle().trim().isEmpty()) {
      throw new IllegalArgumentException("El título no puede estar en blanco.");
    }
  }

  public TaskStats getTaskStatistics() {
    long total = taskRepository.count();
    long completed = taskRepository.countByStatus(TaskStatus.COMPLETED);
    long pending =
        taskRepository.countByStatus(TaskStatus.PENDING)
            + taskRepository.countByStatus(TaskStatus.IN_PROGRESS);
    long overdue =
        taskRepository.countByDueDateBeforeAndStatusNot(LocalDate.now(), TaskStatus.COMPLETED);

    return new TaskStats(total, completed, pending, overdue);
  }
}
