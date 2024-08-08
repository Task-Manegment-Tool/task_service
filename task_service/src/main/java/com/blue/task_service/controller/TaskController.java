package com.blue.task_service.controller;

import com.blue.task_service.dto.TaskDto;
import com.blue.task_service.entity.Task;
import com.blue.task_service.entity.TaskStatus;
import com.blue.task_service.exception.Exception;
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

    @PostMapping("/project/{projectId}")
    public ResponseEntity<Task> createTask(@PathVariable Integer projectId , @RequestBody TaskDto taskDto) throws java.lang.Exception {
        System.out.println(taskDto);
        Task task = taskService.createTask(
                projectId,
                taskDto.getName(),
                taskDto.getDescription(),
                taskDto.getExpiryTime(),
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
    @GetMapping("/projectId/{projectId}")
    public ResponseEntity<List<Task>> getTasksByProjectId(@PathVariable("projectId") Integer projectId){
        List<Task> tasks = taskService.getTasksByProjectId(projectId);


        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/taskId/{taskId}")
    public ResponseEntity<Task> updateTaskById(@PathVariable("taskId") Long taskId, @RequestBody TaskDto taskDto){
        Task task = taskService.updateTaskById(taskId, taskDto);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/taskId/updateStatus/{taskId}")
    public ResponseEntity<String> updateTaskStatusById(@PathVariable("taskId") Long taskId, @RequestBody Task status){

        String task = taskService.updateTaskStatus(taskId,status.getStatus());

        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/taskId/{taskId}")
    public ResponseEntity<Task> deleteTask(@PathVariable("taskId") Long taskId){
        Task task = taskService.deleteTask(taskId);
        return ResponseEntity.ok(task);

    }
    @GetMapping("/task_status_notify/{taskId}")
    public String notifyTaskStatus(@PathVariable("taskId") Long taskId){
         taskService.sendTaskCompletionNotification();

        return "notification send to task pending";}


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleCustomException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}

