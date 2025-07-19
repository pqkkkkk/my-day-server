package org.pqkkkkk.my_day_server.task.dto;

import java.util.Collections;

import org.pqkkkkk.my_day_server.task.entity.List;
import org.pqkkkkk.my_day_server.task.entity.Step;
import org.pqkkkkk.my_day_server.task.entity.Task;

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
public class TaskAggregation {
    List list;
    Task task;
    @Builder.Default
    java.util.List<Step> steps = Collections.emptyList();
}
