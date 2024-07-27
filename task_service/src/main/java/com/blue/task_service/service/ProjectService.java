package com.blue.task_service.service;

import com.blue.task_service.entity.Project;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "project-service")
public interface ProjectService {
    @GetMapping(value = "/project/getProjectById/{projectId}")
     ResponseEntity<Project> getProjectById(@PathVariable("projectId") Integer projectId) throws  Exception;
}
