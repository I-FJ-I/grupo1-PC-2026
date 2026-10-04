package com.example.taskapi.repository;

import com.example.taskapi.model.Task;
import com.example.taskapi.model.TaskPriority;
import com.example.taskapi.model.TaskStatus;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class TaskSpecification {

  private TaskSpecification() {}

  public static Specification<Task> filterBy(
      TaskStatus status, TaskPriority priority, LocalDate dueDate, String title) {
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (status != null) {
        predicates.add(cb.equal(root.get("status"), status));
      }
      if (priority != null) {
        predicates.add(cb.equal(root.get("priority"), priority));
      }
      if (dueDate != null) {
        predicates.add(cb.lessThanOrEqualTo(root.get("dueDate"), dueDate));
      }
      if (title != null && !title.isBlank()) {
        String pattern = "%" + title.trim().toLowerCase() + "%";
        predicates.add(cb.like(cb.lower(root.get("title")), pattern));
      }

      return cb.and(predicates.toArray(Predicate[]::new));
    };
  }
}
