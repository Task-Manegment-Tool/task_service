package com.blue.task_service.dto;

import com.blue.task_service.entity.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskDto {

    private String name;
    private String description;
    private LocalDateTime expiryTime;
    private Long userId;
    private TaskStatus status;
    private Integer projectId;
}
