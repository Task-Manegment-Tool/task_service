package com.blue.task_service.service;

import com.blue.task_service.dto.TaskDto;
import com.blue.task_service.entity.Task;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskService {
    public Task createTask(Integer projectId,String name, String description, LocalDateTime dateTime, Long userId) throws Exception;
    public Task getTaskByTaskId(Long taskId);
    public List<Task> getTasksByUserId(Long userId);
    public List<Task> getTasksByProjectId(Integer projectId);
    public Task updateTaskById(Long taskId, TaskDto taskDto);
    public Task deleteTask(Long taskId);
    public void sendTaskCompletionNotification();
}
