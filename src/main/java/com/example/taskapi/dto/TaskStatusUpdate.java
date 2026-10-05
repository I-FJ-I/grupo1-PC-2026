package com.example.taskapi.dto;

import com.example.taskapi.model.TaskStatus;
import jakarta.validation.constraints.NotNull;

public record TaskStatusUpdate(@NotNull(message = "El estado es obligatorio") TaskStatus status) {}
