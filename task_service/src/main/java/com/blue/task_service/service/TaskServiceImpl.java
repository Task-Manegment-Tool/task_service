package com.blue.task_service.service;

import com.blue.task_service.dto.TaskDto;
import com.blue.task_service.entity.Task;
import com.blue.task_service.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService{

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public Task createTask(String name, String description, LocalDateTime dateTime, Long userId){
        Task task = new Task();
        task.setName(name);
        task.setDescription(description);
        task.setDateTime(dateTime);
        task.setUserId(userId);

        return taskRepository.save(task);
    }

    @Override
    public Task getTaskByTaskId(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        return  task;
    }

    @Override
    public List<Task> getTasksByUserId(Long userId) {
        List<Task> tasks = taskRepository.findAllByUserId(userId);
        return tasks;
    }

    @Override
    public Task updateTaskById(Long taskId, TaskDto taskDto) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task Not Found"));

        task.setTaskId(taskId);
        task.setName(taskDto.getTaskName());
        task.setDescription(taskDto.getDescription());
        task.setDateTime(taskDto.getDateTime());
        task.setUserId(taskDto.getUserId());

        return taskRepository.save(task);
    }

    @Override
    public Task deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Opps !!! This task is not available."));
        taskRepository.deleteById(taskId);
        return task;
    }


}
