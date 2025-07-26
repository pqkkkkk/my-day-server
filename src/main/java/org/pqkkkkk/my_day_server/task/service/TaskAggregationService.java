package org.pqkkkkk.my_day_server.task.service;

import org.pqkkkkk.my_day_server.task.dto.DTO.TaskDTO;
import org.pqkkkkk.my_day_server.task.dto.FilterObject.TaskFilterObject;
import org.springframework.data.domain.Page;

public interface TaskAggregationService {
    public Page<TaskDTO> fetchTaskDTOs(TaskFilterObject filterObject);
}
