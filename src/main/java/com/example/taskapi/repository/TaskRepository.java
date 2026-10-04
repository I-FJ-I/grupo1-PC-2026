package com.example.taskapi.repository;

import com.example.taskapi.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

  long countByStatus(com.example.taskapi.model.TaskStatus status);

  long countByDueDateBeforeAndStatusNot(
      java.time.LocalDate date, com.example.taskapi.model.TaskStatus status);
}
