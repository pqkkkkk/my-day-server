package org.pqkkkkk.my_day_server.task.api;

import org.pqkkkkk.my_day_server.common.ApiResponse;
import org.pqkkkkk.my_day_server.task.api.Request.CreateTaskRequest;
import org.pqkkkkk.my_day_server.task.dto.DTO.TaskDTO;
import org.pqkkkkk.my_day_server.task.dto.FilterObject.TaskFilterObject;
import org.pqkkkkk.my_day_server.task.entity.Task;
import org.pqkkkkk.my_day_server.task.service.TaskAggregationService;
import org.pqkkkkk.my_day_server.task.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/task")
public class TaskApi {
    private final TaskService taskService;
    private final TaskAggregationService taskAggregationService;

    public TaskApi(TaskService taskService, TaskAggregationService taskAggregationService) {
        this.taskService = taskService;
        this.taskAggregationService = taskAggregationService;
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<Page<TaskDTO>>> getTasks( @Valid TaskFilterObject filterObject) {
        Page<TaskDTO> taskDTOs = taskAggregationService.fetchTaskDTOs(filterObject);

        ApiResponse<Page<TaskDTO>> response = new ApiResponse<>(taskDTOs,
                                     true, HttpStatus.OK.value(),
                                     "Tasks fetched successfully");
                                     
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TaskDTO>> createTask(@Valid @RequestBody CreateTaskRequest request) {
        Task task = CreateTaskRequest.toEntity(request);
        Task createdTask = taskService.createTask(task);

        TaskDTO taskDTO = TaskDTO.from(createdTask);

        ApiResponse<TaskDTO> response = new ApiResponse<>(taskDTO,
                                     true, HttpStatus.CREATED.value(),
                                     "Task created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskDTO>> updateTask(@PathVariable Long id,
                                                        @Valid @RequestBody CreateTaskRequest request) {
        Task task = CreateTaskRequest.toEntity(request);
        task.setTaskId(id); // Ensure the task ID is set for update
        Task updatedTask = taskService.updateTask(task);
        
        TaskDTO taskDTO = TaskDTO.from(updatedTask);

        ApiResponse<TaskDTO> response = new ApiResponse<>(taskDTO,
                                     true, HttpStatus.OK.value(),
                                     "Task updated successfully");
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable Long id) {
        Integer deleteResult = taskService.deleteTask(id);
        boolean isDeleted = deleteResult > 0;

        ApiResponse<Void> response = new ApiResponse<>(null,
                                     isDeleted,
                                     isDeleted ?  HttpStatus.NO_CONTENT.value() : HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                     isDeleted ? "Task deleted successfully" : "Task deletion failed");

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
