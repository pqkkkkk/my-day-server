package org.pqkkkkk.my_day_server.task.dto;

import java.time.LocalDate;

public class DTO {
    public record MyListDTO(
        Long listId,
        String listTitle,
        String listDescription,
        String listCategory,
        String color,
        String createdAt,
        String updatedAt,
        String username,
        Integer completedTasksCount,
        Integer totalTasksCount,
        Integer progressPercentage
    ) { 
        public MyListDTO(
            Long listId,
            String listTitle,
            String listDescription,
            String listCategory,
            String color,
            String createdAt,
            String updatedAt,
            String username,
            Integer completedTasksCount,
            Integer totalTasksCount
        ) {
            this(
                listId,
                listTitle,
                listDescription,
                listCategory,
                color,
                createdAt,
                updatedAt,
                username,
                completedTasksCount,
                totalTasksCount,
                totalTasksCount == 0 ? 0 : (completedTasksCount * 100) / totalTasksCount
            );
        }
    }
    public record TaskDTO(
        Long taskId,
        String taskTitle,
        String taskDecription,
        Integer estimatedTime,
        Integer actualTime,
        LocalDate deadline,
        LocalDate createdAt,
        LocalDate updatedAt,
        String taskPriority,
        String taskStatus,
        Long listId,
        String userId,
        Integer totalCompletedSteps,
        Integer totalSteps,
        Integer progressPercentage
    ) {
        public TaskDTO(
            Long taskId,
            String taskTitle,
            String taskDecription,
            Integer estimatedTime,
            Integer actualTime,
            LocalDate deadline,
            LocalDate createdAt,
            LocalDate updatedAt,
            String taskPriority,
            String taskStatus,
            Long listId,
            String userId,
            Integer totalCompletedSteps,
            Integer totalSteps
        ) {
            this(
                taskId,
                taskTitle,
                taskDecription,
                estimatedTime,
                actualTime,
                deadline,
                createdAt,
                updatedAt,
                taskPriority,
                taskStatus,
                listId,
                userId,
                totalCompletedSteps,
                totalSteps,
                totalSteps == 0 ? 0 : (totalCompletedSteps * 100) / totalSteps
            );
        }
    }
}

