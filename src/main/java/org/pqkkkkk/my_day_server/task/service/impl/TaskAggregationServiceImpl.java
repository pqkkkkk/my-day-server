package org.pqkkkkk.my_day_server.task.service.impl;

import org.pqkkkkk.my_day_server.task.dao.task.TaskQueryDao;
import org.pqkkkkk.my_day_server.task.dto.DTO.TaskDTO;
import org.pqkkkkk.my_day_server.task.dto.FilterObject.TaskFilterObject;
import org.pqkkkkk.my_day_server.task.service.TaskAggregationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service("taskAggregationServiceImplV1")
public class TaskAggregationServiceImpl implements TaskAggregationService {
    private final TaskQueryDao taskQueryDao;

    public TaskAggregationServiceImpl(TaskQueryDao taskQueryDao) {
        this.taskQueryDao = taskQueryDao;
    }
    @Override
    public Page<TaskDTO> fetchTaskDTOs(TaskFilterObject filterObject) {
        Direction sortDirection = filterObject.sortDirection().name().equalsIgnoreCase("DESC")
                                ? Direction.DESC : Direction.ASC;

        Sort sort = Sort.by(sortDirection, filterObject.sortBy());
        
        Pageable pageable = PageRequest.of(
            filterObject.currentPage() - 1, // Chuyển từ 1-based sang 0-based cho Spring Data
            filterObject.pageSize(),
            sort
        );

        return taskQueryDao.fetchTaskDTOs(filterObject, pageable);
    }

}
