package org.pqkkkkk.my_day_server.task.api;

import org.pqkkkkk.my_day_server.common.ApiResponse;
import org.pqkkkkk.my_day_server.task.dto.DTO.TaskDTO;
import org.pqkkkkk.my_day_server.task.dto.FilterObject.TaskFilterObject;
import org.pqkkkkk.my_day_server.task.service.TaskAggregationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;


@RestController
@RequestMapping("api/v2/task")
public class TaskApiV2 {
    private final TaskAggregationService taskAggregationService;

    public TaskApiV2(@Qualifier("taskAggregationServiceImplV2") TaskAggregationService taskAggregationService) {
        this.taskAggregationService = taskAggregationService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<TaskDTO>>> getTasks(@Valid TaskFilterObject filterObject) {
        Page<TaskDTO> taskDTOs = taskAggregationService.fetchTaskDTOs(filterObject);

        ApiResponse<Page<TaskDTO>> response = new ApiResponse<>(taskDTOs,
                                     true, HttpStatus.OK.value(),
                                     "Tasks fetched successfully");

        return ResponseEntity.ok(response);
    }
}
