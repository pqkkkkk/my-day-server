package org.pqkkkkk.my_day_server.task.service;

import java.util.List;

import org.pqkkkkk.my_day_server.task.dto.PageResult;
import org.pqkkkkk.my_day_server.task.dto.TaskAggregation;
import org.pqkkkkk.my_day_server.task.dto.FilterObject.TaskFilterObject;

public interface TaskAggregationService {
    public PageResult<TaskAggregation> getTasks(
        TaskFilterObject filterObject
    );
    public TaskAggregation getTaskWithListInfoAndSteps(Long taskId);
    public TaskAggregation getTaskWithListInfor(Long taskId);
    public TaskAggregation getTaskWithSteps(Long taskId);
    public List<TaskAggregation> getUnlistedTasks(
        TaskFilterObject filterObject
    );
}
