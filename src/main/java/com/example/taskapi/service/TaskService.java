package com.example.taskapi.service;

import com.example.taskapi.exception.ResourceNotFoundException;
import com.example.taskapi.model.Task;
import com.example.taskapi.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
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
}