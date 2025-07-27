package org.pqkkkkk.my_day_server.task.dao.task;

import org.pqkkkkk.my_day_server.task.dto.DTO.TaskDTO;
import org.pqkkkkk.my_day_server.task.dto.FilterObject.TaskFilterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskQueryDao {
    public Page<TaskDTO> fetchTaskDTOs(TaskFilterObject filterObject, Pageable pageable);
}
