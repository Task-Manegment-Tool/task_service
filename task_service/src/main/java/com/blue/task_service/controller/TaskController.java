package com.blue.task_service.controller;

import com.blue.task_service.dto.TaskDto;
import com.blue.task_service.entity.Task;
import com.blue.task_service.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody TaskDto taskDto){
        Task task = taskService.createTask(
                taskDto.getTaskName(),
                taskDto.getDescription(),
                taskDto.getDateTime(),
                taskDto.getUserId()
        );

        return ResponseEntity.ok(task);
    }

    @GetMapping("/taskId/{taskId}")
    public ResponseEntity<Task> getTaskByTaskId(@PathVariable("taskId") Long taskId){
        Task task = taskService.getTaskByTaskId(taskId);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/userId/{userId}")
    public ResponseEntity<List<Task>> getTasksByUserId(@PathVariable("userId") Long userId){
        List<Task> tasks = taskService.getTasksByUserId(userId);

        ResponseEntity<List<Task>> response =
                                new ResponseEntity<>(tasks, HttpStatus.OK);

        return response;
    }

    @PutMapping("/taskId/{taskId}")
    public ResponseEntity<Task> updateTaskById(@PathVariable("taskId") Long taskId, @RequestBody TaskDto taskDto){
        Task task = taskService.updateTaskById(taskId, taskDto);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/taskId/{taskId}")
    public ResponseEntity<Task> deleteTask(@PathVariable("taskId") Long taskId){
        Task task = taskService.deleteTask(taskId);
        return ResponseEntity.ok(task);
    }
}
