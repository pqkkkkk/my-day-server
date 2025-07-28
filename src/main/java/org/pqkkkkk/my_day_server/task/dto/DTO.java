package org.pqkkkkk.my_day_server.task.dto;

import java.util.List;

import org.pqkkkkk.my_day_server.task.entity.MyList;
import org.pqkkkkk.my_day_server.task.entity.Step;
import org.pqkkkkk.my_day_server.task.entity.Task;

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
        public static MyListDTO from(MyList list) {
            return new MyListDTO(
                list.getListId(),
                list.getListTitle(),
                list.getListDescription(),
                list.getListCategory().name(),
                list.getColor(),
                list.getCreatedAt() != null ? list.getCreatedAt().toString() : null,
                list.getUpdatedAt() != null ? list.getUpdatedAt().toString() : null,
                list.getUser().getUsername(),
                0,
                0,
                0
            );
        }
    }
    public record TaskDTO(
        Long taskId,
        String taskTitle,
        String taskDecription,
        Integer estimatedTime,
        Integer actualTime,
        String deadline,
        String createdAt,
        String updatedAt,
        String taskPriority,
        String taskStatus,
        Long listId,
        String userId,
        Integer totalCompletedSteps,
        Integer totalSteps,
        Integer progressPercentage,
        List<StepDTO> steps
    ) {
        public TaskDTO(
            Long taskId,
            String taskTitle,
            String taskDecription,
            Integer estimatedTime,
            Integer actualTime,
            String deadline,
            String createdAt,
            String updatedAt,
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
                totalSteps == 0 ? 0 : (totalCompletedSteps * 100) / totalSteps,
                List.of() // Default to empty list for steps
            );
        }
        public static TaskDTO from(Task task){
            return new TaskDTO(
                task.getTaskId(),
                task.getTaskTitle(),
                task.getTaskDescription(),
                task.getEstimatedTime(),
                task.getActualTime(),
                task.getDeadline() != null ? task.getDeadline().toString() : null,
                task.getCreatedAt() != null ? task.getCreatedAt().toString() : null,
                task.getUpdatedAt() != null ? task.getUpdatedAt().toString() : null,
                task.getTaskPriority() != null ? task.getTaskPriority().name() : null,
                task.getTaskStatus() != null ? task.getTaskStatus().name() : null,
                task.getList() != null ? task.getList().getListId() : null,
                task.getUser() != null ? task.getUser().getUsername() : null,
                0,
                0,
                0,
                List.of() // Default to empty list for steps
            );
        }
        public static TaskDTO fromOldAndSteps(TaskDTO oldTaskDTO, List<StepDTO> steps) {
            return new TaskDTO(
                oldTaskDTO.taskId(),
                oldTaskDTO.taskTitle(),
                oldTaskDTO.taskDecription(),
                oldTaskDTO.estimatedTime(),
                oldTaskDTO.actualTime(),
                oldTaskDTO.deadline(),
                oldTaskDTO.createdAt(),
                oldTaskDTO.updatedAt(),
                oldTaskDTO.taskPriority(),
                oldTaskDTO.taskStatus(),
                oldTaskDTO.listId(),
                oldTaskDTO.userId(),
                Long.valueOf(steps.stream().filter(StepDTO::completed).count()).intValue(), // Count completed steps
                steps.size(), // Total steps
                steps.isEmpty() ? 0 : (int) ((steps.stream().filter(StepDTO::completed).count() * 100) / steps.size()), // Progress percentage
                steps
            );
        }
    }

    public record StepDTO(
        Long stepId,
        String stepTitle,
        String createdAt,
        boolean completed,
        String taskId
    ) {
        public static StepDTO fromEntity(Step step) {
            return new StepDTO(
                step.getStepId(),
                step.getStepTitle(),
                step.getCreatedAt() != null ? step.getCreatedAt().toString() : null,
                step.getCompleted(),
                step.getTask() != null ? step.getTask().getTaskId().toString() : null
            );
        }
    }
}

