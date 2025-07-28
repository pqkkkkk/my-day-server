package org.pqkkkkk.my_day_server.service_test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pqkkkkk.my_day_server.TestSecurityConfig;
import org.pqkkkkk.my_day_server.data_builder.StepTestDataBuilder;
import org.pqkkkkk.my_day_server.data_builder.TaskDataBuidler;
import org.pqkkkkk.my_day_server.data_builder.UserTestDataBuilder;
import org.pqkkkkk.my_day_server.task.Constants.TaskPriority;
import org.pqkkkkk.my_day_server.task.dto.DTO.TaskDTO;
import org.pqkkkkk.my_day_server.task.dto.FilterObject.TaskFilterObject;
import org.pqkkkkk.my_day_server.task.entity.Step;
import org.pqkkkkk.my_day_server.task.entity.Task;
import org.pqkkkkk.my_day_server.task.service.TaskAggregationService;
import org.pqkkkkk.my_day_server.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@Import(TestSecurityConfig.class) // Import security configuration if needed
@ActiveProfiles("test")  // Only applies to this class
public class TaskAggregationServiceImplV2IntegrationTest {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    @Qualifier("taskAggregationServiceImplV2")
    private TaskAggregationService taskAggregationService;
    
    @BeforeEach
    public void setUp(){
        User user = UserTestDataBuilder.createTestUser();
        entityManager.persist(user);

        Task mockTask1 = TaskDataBuidler.createValidTaskWithoutList(user);
        mockTask1.setTaskPriority(TaskPriority.HIGH);
        Step stepOfTask1 = StepTestDataBuilder.createValidStep();
        stepOfTask1.setTask(mockTask1);
        mockTask1.setSteps(List.of(stepOfTask1));
        entityManager.persist(mockTask1);

        Task mockTask2 = TaskDataBuidler.createValidTaskWithoutList(user);
        mockTask1.setTaskPriority(TaskPriority.HIGH);
        Step stepOfTask2 = StepTestDataBuilder.createValidStep();
        stepOfTask2.setTask(mockTask2);
        mockTask2.setSteps(List.of(stepOfTask2));

        entityManager.persist(mockTask2);

        entityManager.flush(); // Ensure all entities are persisted before tests
    }
    // ---- Test cases for fetchTaskDTOs ----
    @Test
    public void fetchTaskDTOs_WithValidFilter_SholdReturnTasksWithSteps() {
        // Given
        // Create a valid TaskFilterObject
        TaskFilterObject filterObject = TaskDataBuidler.getTaskFilterObject();

        // When
        // Call the fetchTaskDTOs method with the filter object
        Page<TaskDTO> result = taskAggregationService.fetchTaskDTOs(filterObject);

        // Then
        // Assert that the result contains tasks with steps
        assert !result.getContent().isEmpty();
        for (var taskDTO : result.getContent()) {
            assertEquals(!taskDTO.steps().isEmpty(), true);
            assertEquals(taskDTO.totalSteps(), 1);
            assertEquals(taskDTO.totalCompletedSteps(), 0);
            assertEquals(taskDTO.taskPriority(), TaskPriority.HIGH.name());
        }
    }
}
