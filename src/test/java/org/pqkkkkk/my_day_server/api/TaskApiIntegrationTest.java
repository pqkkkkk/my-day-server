package org.pqkkkkk.my_day_server.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.pqkkkkk.my_day_server.TestSecurityConfig;
import org.pqkkkkk.my_day_server.common.ApiError;
import org.pqkkkkk.my_day_server.common.ApiResponse;
import org.pqkkkkk.my_day_server.task.Constants.TaskPriority;
import org.pqkkkkk.my_day_server.task.api.Request.CreateTaskRequest;
import org.pqkkkkk.my_day_server.task.dto.DTO.TaskDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import jakarta.transaction.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@Transactional
public class TaskApiIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createTask_WithInvalidRequest_ShouldReturnBadRequest(){
        CreateTaskRequest request = new CreateTaskRequest(
            null, // Invalid title
            "This is a task description",
            null, // No priority
            null, // No list ID
            null,
            null,
            null,
            null
        );

        // When
        ResponseEntity<ApiError> response = restTemplate.postForEntity("/api/v1/task", request, ApiError.class);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    public void createTask_WithValidRequest_ShouldReturnCreated() {
        // Given        
        CreateTaskRequest request = new CreateTaskRequest(
            "Test Task",
            "This is a task description",
            TaskPriority.HIGH,
            null,
            null,
            null,
            null,
            null
        );

        // When
        ResponseEntity<ApiResponse<TaskDTO>> response = restTemplate.postForEntity("/api/v1/task", request, (Class<ApiResponse<TaskDTO>>) (Class<?>) ApiResponse.class);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}
