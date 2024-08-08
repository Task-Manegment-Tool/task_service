package com.blue.task_service.service;

import com.blue.task_service.dto.TaskDto;
import com.blue.task_service.entity.Mail;
import com.blue.task_service.entity.Project;
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
import java.util.Optional;
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
    @Autowired
    private ProjectService projectService;

    @Override
    public Task createTask(Integer projectid,String name, String description, LocalDateTime dateTime, Long userId) throws Exception {
        Project project =projectService.getProjectById(projectid).getBody();

        if(project == null){
//            Exception e = new RuntimeException("Project not found");
//            throw e;
            Project project1 = new Project();
            project1.setProjectName("project not found");
            project1.setDomain("send proper project id");
            Task task = new Task();
            task.setProjectId(project1.getId());
            return task;
        }

        Task task = new Task();

        task.setName(name);
        task.setDescription(description);
        task.setExpiryTime(dateTime);
        task.setUserId(userId);
        task.setStatus(TaskStatus.BACK_LOG);

        task.setProjectId(project.getId());
        System.out.println(task);
       Task task1 = taskRepository.save(task);
        System.out.println(task1);


//        Mail mail = new Mail();
//        mail.setTo("palaskara749@gmail.com");
//        mail.setSubject("New Task Assinged");
//        mail.setText("Hello User "+task.getUserId()+"!!\r \n" +
//                "A New task is assinged tp you , this mail is to let u know your task \r\n " +
//                "Task Name : "+task.getName()+" is assinged . \r\n " +
//                "Task Status : " +task.getStatus()+"\r\n"+
//                "Task Description : "+task.getDescription()+" and its expired time is "+task.getExpiryTime() );


        try {

            task1.setId(Long.valueOf("100"+task.getId()));

            String taskJson = objectMapper.writeValueAsString(task1);

           template.convertAndSend(topic.getTopic(), taskJson);
        }
        catch(Exception e){
            e.printStackTrace();
        }

       // notify.sendNotification(mail);

//       return taskRepository.save(task);
       return task1;
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
    public List<Task> getTasksByProjectId(Integer projectId) {
        List<Task> listofTask = taskRepository.findAllByProjectId(projectId);

        return listofTask;
    }

    @Override
    public Task updateTaskById(Long taskId, TaskDto taskDto) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task Not Found"));

        task.setId(taskId);
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setExpiryTime(taskDto.getExpiryTime());
        task.setUserId(taskDto.getUserId());
        task.setStatus(taskDto.getStatus());

        task.setProjectId(taskDto.getProjectId());
        Task taskUpdated =taskRepository.save(task);

try {
             taskUpdated.setId(Long.valueOf("101"+task.getId()));

           String taskJson = objectMapper.writeValueAsString(taskUpdated);
           template.convertAndSend(topic.getTopic(), taskJson);
       }
       catch(Exception e){
           e.printStackTrace();
       }

      //  notify.sendNotification(mail);

        return task;
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

       List<Task> listOfPendingTask = taskRepository.findByStatus(TaskStatus.BACK_LOG);
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

                        expiredTask.setId(Long.valueOf("102"+expiredTask.getId()));

                        System.out.println(expiredTask.getId());

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

    @Override
    public String updateTaskStatus(Long taskId, TaskStatus status) {

        Optional<Task> task = taskRepository.findById(taskId);
       if(!task.isPresent()){
           return "task not present";
       }
       TaskStatus previousStatus = task.get().getStatus();
       task.get().setStatus(status);
       taskRepository.save(task.get());
        return "task uodated \r\n" +
                "task id: " +taskId+" \r\n"+
                "its status change from  "+previousStatus+ " to "+task.get().getStatus();
    }
}






