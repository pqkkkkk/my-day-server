package org.pqkkkkk.my_day_server.entity;

import java.util.Date;

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
public class Step {
    @Column(isPrimaryKey = true, insertable = false, updatable = false, value = "step_id")
    Long stepId;
    @Column("step_title")
    String stepTitle;
    @Column("completed")
    boolean completed;
    Date createdAt;
    @Column("task_id")
    int taskId; // Foreign key to Task entity
}
