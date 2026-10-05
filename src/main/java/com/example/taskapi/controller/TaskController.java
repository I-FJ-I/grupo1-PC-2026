package com.example.taskapi.controller;

import com.example.taskapi.dto.TaskStats;
import com.example.taskapi.model.Task;
import com.example.taskapi.model.TaskPriority;
import com.example.taskapi.model.TaskStatus;
import com.example.taskapi.service.TaskService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping 
  public ResponseEntity<List<Task>> getTasks(
      @RequestParam(required = false) TaskStatus status,
      @RequestParam(required = false) TaskPriority priority,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate dueDate,
      @RequestParam(required = false) String title) {
    return ResponseEntity.ok(taskService.searchTasks(status, priority, dueDate, title));
  }

  @GetMapping("/stats")
  public ResponseEntity<TaskStats> getTaskStats() {
    return ResponseEntity.ok(taskService.getTaskStatistics());
  }

  public ResponseEntity<Page<Task>> getAllTasks(@PageableDefault(size = 20) Pageable pageable) {
    return ResponseEntity.ok(taskService.getAllTasks(pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
    return ResponseEntity.ok(taskService.getTaskById(id));
  }

  @PostMapping
  public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
    return new ResponseEntity<>(taskService.createTask(task), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody Task task) {
    return ResponseEntity.ok(taskService.updateTask(id, task));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
    taskService.deleteTask(id);
    return ResponseEntity.noContent().build();
  }
}
