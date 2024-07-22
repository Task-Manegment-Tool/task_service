package com.blue.task_service.service;

import com.blue.task_service.dto.TaskDto;
import com.blue.task_service.entity.Mail;
import com.blue.task_service.entity.Task;
import com.blue.task_service.entity.TaskStatus;
import com.blue.task_service.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService{

    @Autowired
    private RedisTemplate template;

    @Autowired
    private ChannelTopic topic;
    @Autowired
    private  ObjectMapper  objectMapper;

//    private final ObjectMapper objectMapper = new ObjectMapper();
//    objectMapper.registerModule(new JavaTimeModule());


    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private  NotificationService notify;

    @Override
    public Task createTask(String name, String description, LocalDateTime dateTime, Long userId){
        Task task = new Task();
        task.setName(name);
        task.setDescription(description);
        task.setExpiryTime(dateTime);
        task.setUserId(userId);
        task.setStatus(TaskStatus.PENDING);

        Mail mail = new Mail();
        mail.setTo("palaskara749@gmail.com");
        mail.setSubject("New Task Assinged");
        mail.setText("Hello User "+task.getUserId()+"!!\r \n" +
                "A New task is assinged tp you , this mail is to let u know your task \r\n " +
                "Task Name : "+task.getName()+" is assinged . \r\n " +
                "Task Status : " +task.getStatus()+"\r\n"+
                "Task Description : "+task.getDescription()+" and its expired time is "+task.getExpiryTime() );
        notify.sendNotification(mail);

       return taskRepository.save(task);
       // return task;
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
        task.setExpiryTime(taskDto.getDateTime());
        task.setUserId(taskDto.getUserId());
        task.setStatus(taskDto.getStatus());


        Mail mail = new Mail();
        mail.setTo("palaskara749@gmail.com");
        mail.setSubject("Task Updated");
        mail.setText("Hello User "+task.getUserId()+"!!\r \n" +
                "Your task is updated this mail is to let u know ypur updated task \r\n " +
                "Task Name : "+task.getName()+" is updated. \r\n " +
                "Task Status : " +task.getStatus()+"\r\n"+
                "Task Description : "+task.getDescription()+" and its expired time is "+task.getExpiryTime());
        notify.sendNotification(mail);

        return taskRepository.save(task);
     //   return task;
    }

    @Override
    public Task deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Opps !!! This task is not available."));
        taskRepository.deleteById(taskId);
        return task;
    }

    @Override
    @Scheduled(fixedRate = 60000) // Schedule to run every minute
    public void sendTaskCompletionNotification() {

       List<Task> listOfPendingTask = taskRepository.findByStatus(TaskStatus.PENDING);
       String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        List<Task> listOfExpiredTask =  listOfPendingTask.stream()
                .filter(task -> task.getExpiryTime().format(formatter).equals(currentTime))
                .collect(Collectors.toList());

        System.out.println(listOfExpiredTask);
                for(Task expiredTask : listOfExpiredTask) {

                    try{
                        System.out.println("inside loop");
                        System.out.println(expiredTask);
                        String taskJson = objectMapper.writeValueAsString(expiredTask);



                        template.convertAndSend(topic.getTopic(), taskJson);

                        System.out.println("message publish ");





                        }
                        catch(Exception e ){
                            Thread.currentThread().interrupt();
                            e.printStackTrace();

                        }

                }

            }
        }






