package com.blue.task_service.dto;

import com.blue.task_service.entity.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskDto {
    private String taskName;
    private String description;
    private LocalDateTime dateTime;
    private Long userId;
    private TaskStatus status;
}
