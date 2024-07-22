package com.blue.task_service.service;

import com.blue.task_service.entity.Mail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service")
public interface NotificationService {

    @PostMapping("/notify/send")
   void sendNotification(@RequestBody Mail mail);
}
