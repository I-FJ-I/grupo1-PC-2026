package com.example.taskapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "tasks")
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "El título es obligatorio")
  @Size(max = 100, message = "El título no puede exceder los 100 caracteres")
  private String title;

  @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres")
  private String description;

  @NotNull(message = "El estado es obligatorio")
  @Enumerated(EnumType.STRING)
  private TaskStatus status;

  @NotNull(message = "La prioridad es obligatoria")
  @Enumerated(EnumType.STRING)
  private TaskPriority priority;

  @NotNull(message = "La fecha límite es obligatoria")
  @FutureOrPresent(message = "La fecha límite no puede estar en el pasado")
  private LocalDate dueDate;

  // Comentario rama 2

  public Task() {}

  public Task(
      String title,
      String description,
      TaskStatus status,
      TaskPriority priority,
      LocalDate dueDate) {
    this.title = title;
    this.description = description;
    this.status = status;
    this.priority = priority;
    this.dueDate = dueDate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public TaskStatus getStatus() {
    return status;
  }

  public void setStatus(TaskStatus status) {
    this.status = status;
  }

  public TaskPriority getPriority() {
    return priority;
  }

  public void setPriority(TaskPriority priority) {
    this.priority = priority;
  }

  public LocalDate getDueDate() {
    return dueDate;
  }

  public void setDueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
  }
}
