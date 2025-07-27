package org.pqkkkkk.my_day_server.task.api;

import java.time.LocalDateTime;

import org.pqkkkkk.my_day_server.task.Constants.ListCategory;
import org.pqkkkkk.my_day_server.task.Constants.TaskPriority;
import org.pqkkkkk.my_day_server.task.entity.MyList;
import org.pqkkkkk.my_day_server.task.entity.Step;
import org.pqkkkkk.my_day_server.task.entity.Task;
import org.pqkkkkk.my_day_server.user.entity.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Request {
    public record CreateListRequest(

        @NotBlank
        @Size(max = 100)
        String listTitle,

        @NotBlank
        String listDescription,

        ListCategory listCategory,

        @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")
        String color,

        @NotBlank
        String username
    ) {
        public static MyList toEntity(CreateListRequest request) {
            User user = new User();
            user.setUsername(request.username);
            return MyList.builder()
                .listTitle(request.listTitle)
                .listDescription(request.listDescription)
                .listCategory(request.listCategory)
                .color(request.color)
                .user(user)
                .build();
        }
    }

    public record CreateTaskRequest(
        @NotBlank
        String taskTitle,

        String taskDescription,

        TaskPriority taskPriority,

        Integer estimatedTime,

        Integer actualTime,

        LocalDateTime deadline,

        Long listId,

        String username
    ) {
        public static Task toEntity(CreateTaskRequest request) {
            Task task = Task.builder()
                .taskTitle(request.taskTitle)
                .taskDescription(request.taskDescription)
                .taskPriority(request.taskPriority)
                .estimatedTime(request.estimatedTime)
                .actualTime(request.actualTime)
                .deadline(request.deadline)
                .build();

            if( request.listId != null) {
                MyList list = new MyList();
                list.setListId(request.listId);
                task.setList(list);
            }
            if (request.username != null) {
                User user = new User();
                user.setUsername(request.username);
                task.setUser(user);
            }

            return task;
        }
    }

    public record CreateStepRequest(
        @NotBlank
        String stepTitle
    ){
        public static Step toEntity(CreateStepRequest request) {
            return Step.builder()
                .stepTitle(request.stepTitle)
                .build();
        }
    }
}
