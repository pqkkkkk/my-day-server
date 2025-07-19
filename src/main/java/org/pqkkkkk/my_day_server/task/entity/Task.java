package org.pqkkkkk.my_day_server.task.entity;

import java.util.Date;

import org.pqkkkkk.my_day_server.task.Constants.TaskPriority;
import org.pqkkkkk.my_day_server.task.Constants.TaskStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Task {
    @Column(isPrimaryKey = true, insertable = false, updatable = false, value = "task_id")
    Long taskId;

    @Column("task_title")
    String taskTitle;

    @Column("task_description")
    String taskDescription;

    @Column("estimated_time")
    int estimatedTime; // in minutes

    @Column("actual_time")
    int actualTime; // in minutes

    Date deadline;

    @Column("task_priority")
    TaskPriority taskPriority;

    @Column("task_status")
    TaskStatus taskStatus;

    @Column("progress")
    int progress; // percentage of completion

    Date createdAt;
    Date updatedAt;

    @Column("list_id")
    int listId; // Foreign key to List entity
}
