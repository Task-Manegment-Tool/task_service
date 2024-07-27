package com.blue.task_service.repository;

import com.blue.task_service.entity.Task;
import com.blue.task_service.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByProjectId(Integer projectId);
    List<Task> findAllByUserId(Long userId);
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByexpiryTime(LocalDateTime currentTime);

}
