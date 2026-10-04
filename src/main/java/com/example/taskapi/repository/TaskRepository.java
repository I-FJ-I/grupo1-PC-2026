package com.example.taskapi.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.taskapi.model.Task;
import com.example.taskapi.model.TaskStatus;

@Repository

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task>{

  long countByStatus(TaskStatus status);

  long countByDueDateBeforeAndStatusNot(LocalDate date, TaskStatus status);
}
