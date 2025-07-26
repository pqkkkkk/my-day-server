package org.pqkkkkk.my_day_server.task.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.pqkkkkk.my_day_server.task.Constants.ListCategory;
import org.pqkkkkk.my_day_server.task.Constants.SortDirection;
import org.pqkkkkk.my_day_server.task.Constants.TaskPriority;
import org.pqkkkkk.my_day_server.task.Constants.TaskStatus;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class FilterObject {
    public record ListFilterObject(
        @NotNull(message = "Current page must not be null")
        @Min(value = 1, message = "Current page must be greater than or equal to 1")
        Integer currentPage,

        @NotNull(message = "Page size must not be null")
        @Min(value = 1, message = "Page size must be greater than or equal to 1")
        Integer pageSize,

        @Pattern(regexp = "^(listId|listTitle|createdAt)$", message = "Invalid sortBy value")
        String sortBy,

        SortDirection sortDirection,

        @Size(min = 1, max = 100, message = "List title must be between 1 and 100 characters")
        String listTitle,

        ListCategory listCategory,

        @PastOrPresent(message = "Created At From date must be in the past or present")
        LocalDateTime createdAtFrom,

        LocalDateTime createdAtTo
    ) {
        public ListFilterObject{
            currentPage = currentPage != null ? currentPage : 1;
            pageSize = pageSize != null ? pageSize : 10;
        }
    }

    public record TaskFilterObject(
        @NotNull(message = "Current page must not be null")
        @Min(value = 1, message = "Current page must be greater than or equal to 1")
        Integer currentPage,

        @NotNull(message = "Page size must not be null")
        @Min(value = 1, message = "Page size must be greater than or equal to 1")
        Integer pageSize,

        @Pattern(regexp = "^(taskId|taskTitle|createdAt|deadline)$", message = "Invalid sortBy value")
        String sortBy,

        SortDirection sortDirection,

        @Min(value = 1, message = "List ID must be greater than or equal to 1")
        Long listId,

        String username,
        @Size(min = 1, max = 100, message = "Task title must be between 1 and 100 characters")
        String taskTitle,

        TaskStatus taskStatus,

        TaskPriority taskPriority,

        @PastOrPresent(message = "Created At From date must be in the past or present")
        LocalDate createdAtFrom,

        LocalDate createdAtTo
    ) {
        public TaskFilterObject{
            currentPage = currentPage != null ? currentPage : 1;
            pageSize = pageSize != null ? pageSize : 10;
            sortBy = sortBy != null ? sortBy : "taskId";
            sortDirection = sortDirection != null ? sortDirection : SortDirection.DESC;
        }
    }

}
