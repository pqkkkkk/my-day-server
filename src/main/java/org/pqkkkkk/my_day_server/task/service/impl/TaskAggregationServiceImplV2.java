package org.pqkkkkk.my_day_server.task.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.pqkkkkk.my_day_server.task.dao.step.StepQueryDao;
import org.pqkkkkk.my_day_server.task.dao.task.TaskQueryDao;
import org.pqkkkkk.my_day_server.task.dto.DTO.StepDTO;
import org.pqkkkkk.my_day_server.task.dto.DTO.TaskDTO;
import org.pqkkkkk.my_day_server.task.dto.FilterObject.TaskFilterObject;
import org.pqkkkkk.my_day_server.task.entity.Step;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

@Service("taskAggregationServiceImplV2")
public class TaskAggregationServiceImplV2 extends TaskAggregationServiceImpl {
    private final StepQueryDao stepQueryDao;

    public TaskAggregationServiceImplV2(TaskQueryDao taskQueryDao, StepQueryDao stepQueryDao) {
        super(taskQueryDao);
        this.stepQueryDao = stepQueryDao;
    }
    @Override
    public Page<TaskDTO> fetchTaskDTOs(TaskFilterObject filterObject) {
        // Call the parent method to fetch task DTOs
        Page<TaskDTO> taskPage = super.fetchTaskDTOs(filterObject);
        
        // Map steps to tasks
        List<TaskDTO> taskDTOsWithSteps = mapStepsToTasks(taskPage.getContent());
        
        // Return a new Page with the updated content
        return new PageImpl<>(taskDTOsWithSteps, taskPage.getPageable(), taskPage.getTotalElements());
    }
    private List<TaskDTO> mapStepsToTasks(List<TaskDTO> taskDTOs) {
        List<Long> taskIds = taskDTOs.stream()
            .map(TaskDTO::taskId)
            .collect(Collectors.toList());

        List<Step> steps = stepQueryDao.findAllByTaskIds(taskIds);

        Map<Long, List<Step>> stepsByTaskId = steps.stream()
            .collect(Collectors.groupingBy(step -> step.getTask().getTaskId()));

        List<TaskDTO> result = taskDTOs.stream().map(taskDTO -> {
            List<StepDTO> taskSteps = stepsByTaskId.getOrDefault(taskDTO.taskId(), Collections.emptyList())
                .stream()
                .map(StepDTO::fromEntity)
                .collect(Collectors.toList());

            return TaskDTO.fromOldAndSteps(taskDTO, taskSteps);
        }).collect(Collectors.toList());

        return result;
    }

}
