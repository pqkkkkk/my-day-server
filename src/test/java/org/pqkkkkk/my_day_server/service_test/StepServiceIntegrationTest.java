package org.pqkkkkk.my_day_server.service_test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import org.junit.jupiter.api.Test;
import org.pqkkkkk.my_day_server.TestSecurityConfig;
import org.pqkkkkk.my_day_server.common.EntityNotFoundException;
import org.pqkkkkk.my_day_server.data_builder.TaskDataBuidler;
import org.pqkkkkk.my_day_server.data_builder.UserTestDataBuilder;
import org.pqkkkkk.my_day_server.task.entity.Step;
import org.pqkkkkk.my_day_server.task.entity.Task;
import org.pqkkkkk.my_day_server.task.service.StepService;
import org.pqkkkkk.my_day_server.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@Import(TestSecurityConfig.class) // Import security configuration if needed
@Profile("test") // Only applies to this class
public class StepServiceIntegrationTest {

    @Autowired
    private StepService stepService;
    @Autowired
    private EntityManager entityManager;

    // --------- Test cases for create operation -----------

    @Test
    public void createStep_WithValidData_ShouldCreateStep() {
        // Given
        User user = UserTestDataBuilder.createTestUser();
        entityManager.persist(user);
        Task task = TaskDataBuidler.createValidTaskWithoutList(user);
        entityManager.persist(task);
        
        Step step = Step.builder()
            .stepTitle("Integration Test Step")
            .task(task)
            .build();
        
        // When
        Step createdStep = stepService.createStep(step);
        
        // Then
        assertNotNull(createdStep.getStepId());
        Step foundStep = entityManager.find(Step.class, createdStep.getStepId());
        assertNotNull(foundStep);
    }

    // ----------- Test cases for update operation -----------
    @Test
    public void updateStep_WithValidData_ShouldUpdateStep() {
        // Given
        User user = UserTestDataBuilder.createTestUser();
        entityManager.persist(user);
        Task task = TaskDataBuidler.createValidTaskWithoutList(user);
        entityManager.persist(task);
        
        Step step = Step.builder()
            .stepTitle("Integration Test Step")
            .task(task)
            .build();
        Step createdStep = stepService.createStep(step);
        
        // When
        createdStep.setStepTitle("Updated Step Title");
        Step updatedStep = stepService.updateStep(createdStep);
        
        // Then
        assertNotNull(updatedStep.getStepId());
        assertEquals("Updated Step Title", updatedStep.getStepTitle());
    }
    @Test
    public void updateStep_WithNonExistingId_ShouldThrowException() {
        // Given
        User user = UserTestDataBuilder.createTestUser();
        entityManager.persist(user);
        Task task = TaskDataBuidler.createValidTaskWithoutList(user);
        entityManager.persist(task);
        
        Step step = Step.builder()
            .stepTitle("Integration Test Step")
            .task(task)
            .build();
        step.setStepId(999L); // Non-existing ID
        
        // When & Then
        assertThrowsExactly(EntityNotFoundException.class, () -> stepService.updateStep(step));
    }

    // --------- Test cases for delete operation -----------
    @Test
    public void deleteStep_WithValidStep_ShouldDeleteStep() {
        // Given
        User user = UserTestDataBuilder.createTestUser();
        entityManager.persist(user);
        
        Task task = TaskDataBuidler.createValidTaskWithoutList(user);
        entityManager.persist(task);
        
        Step step = Step.builder()
            .stepTitle("Integration Test Step")
            .task(task)
            .build();
        Step createdStep = stepService.createStep(step);
        
        // When
        stepService.deleteStep(createdStep.getStepId());
        
        // Then
        Step deletedStep = entityManager.find(Step.class, createdStep.getStepId());
        assertNull(deletedStep, "Deleted step should not be found");
    }
    @Test
    public void deleteStep_WithNonExistingId_ShouldThrowException() {
        // Given
        Step step = Step.builder()
            .stepTitle("Integration Test Step")
            .build();
        step.setStepId(999L); // Non-existing ID
        
        // When & Then
        assertThrowsExactly(EntityNotFoundException.class, () -> stepService.deleteStep(step.getStepId()));
    }
}
