package org.pqkkkkk.my_day_server.data_builder;

import java.time.LocalDateTime;
import java.util.List;

import org.pqkkkkk.my_day_server.task.Constants.TaskPriority;
import org.pqkkkkk.my_day_server.task.Constants.TaskStatus;
import org.pqkkkkk.my_day_server.task.dto.FilterObject.TaskFilterObject;
import org.pqkkkkk.my_day_server.task.entity.MyList;
import org.pqkkkkk.my_day_server.task.entity.Task;
import org.pqkkkkk.my_day_server.user.entity.User;


public class TaskDataBuidler {
    public static TaskFilterObject getTaskFilterObject( ) {
        return new TaskFilterObject(
            1,
            10,
            null,
            null,
            null, null,
            null, TaskStatus.TODO, TaskPriority.HIGH,
            null, null
        );
    }
    public static Task createValidTaskWithList(MyList list){
        LocalDateTime now = LocalDateTime.now();
        return Task.builder()
            .taskTitle("Valid Task")
            .taskDescription("This is a valid task description.")
            .estimatedTime(60)
            .actualTime(50)
            .deadline(now.plusDays(7))
            .taskPriority(TaskPriority.HIGH)
            .taskStatus(TaskStatus.TODO)
            .list(list)
            .build();
    }
    public static Task createValidTaskWithoutList(User user){
        LocalDateTime now = LocalDateTime.now();
        return Task.builder()
            .taskTitle("Valid Task Without List")
            .taskDescription("This is a valid task description without a list.")
            .estimatedTime(60)
            .actualTime(50)
            .deadline(now.plusDays(7))
            .taskPriority(TaskPriority.MEDIUM)
            .taskStatus(TaskStatus.TODO)
            .user(user)
            .build();
    }
    public static List<Task> getTasks(MyList myList, User user) {
        LocalDateTime now = LocalDateTime.now();
        return List.of(
            Task.builder()
                .taskTitle("Task 1")
                .taskDescription("Description for Task 1")
                .estimatedTime(30)
                .actualTime(25)
                .deadline(now.plusDays(30))
                .taskPriority(TaskPriority.HIGH)
                .taskStatus(TaskStatus.TODO)
                .list(myList)
                .user(user)
                .build(),
            Task.builder()
                .taskTitle("Task 2")
                .taskDescription("Description for Task 2")
                .estimatedTime(45)
                .actualTime(40)
                .deadline(now.plusDays(30))
                .taskPriority(TaskPriority.MEDIUM)
                .taskStatus(TaskStatus.IN_PROGRESS)
                .list(myList)
                .user(user)
                .build()
            );
    }
}
