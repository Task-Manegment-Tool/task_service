package com.blue.task_service.service;

import com.blue.task_service.dto.TaskDto;
import com.blue.task_service.entity.Task;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskService {
    public Task createTask(String name, String description, LocalDateTime dateTime, Long userId);
    public Task getTaskByTaskId(Long taskId);
    public List<Task> getTasksByUserId(Long userId);
    public Task updateTaskById(Long taskId, TaskDto taskDto);
    public Task deleteTask(Long taskId);
}
