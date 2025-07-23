package org.pqkkkkk.my_day_server.task.api;

import org.pqkkkkk.my_day_server.common.ApiResponse;
import org.pqkkkkk.my_day_server.task.entity.Task;
import org.pqkkkkk.my_day_server.task.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    public TaskApi(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Task>> createTask(@Valid @RequestBody Task task) {
        Task createdTask = taskService.createTask(task);
        ApiResponse<Task> response = new ApiResponse<>(createdTask,
                                     true, HttpStatus.CREATED.value(),
                                     "Task created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Task>> updateTask(@PathVariable Long id,
                                                        @Valid @RequestBody Task task) {
        task.setTaskId(id);
        Task updatedTask = taskService.updateTask(task);
        ApiResponse<Task> response = new ApiResponse<>(updatedTask,
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
