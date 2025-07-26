package org.pqkkkkk.my_day_server.task.dao.task;

import org.pqkkkkk.my_day_server.task.dao.jpa_repository.TaskRepository;
import org.pqkkkkk.my_day_server.task.dto.DTO.TaskDTO;
import org.pqkkkkk.my_day_server.task.dto.FilterObject.TaskFilterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class TaskQueryJpaDao implements TaskQueryDao {
    private final TaskRepository taskRepository;

    public TaskQueryJpaDao(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Page<TaskDTO> fetchTaskDTOs(TaskFilterObject filterObject, Pageable pageable) {
        return taskRepository.fetchTaskDTOs(filterObject, pageable);
    }
}
