package org.pqkkkkk.my_day_server.dao_test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.pqkkkkk.my_day_server.data_builder.StepTestDataBuilder;
import org.pqkkkkk.my_day_server.data_builder.TaskDataBuidler;
import org.pqkkkkk.my_day_server.data_builder.UserTestDataBuilder;
import org.pqkkkkk.my_day_server.task.dao.jpa_repository.StepRepository;
import org.pqkkkkk.my_day_server.task.entity.Step;
import org.pqkkkkk.my_day_server.task.entity.Task;
import org.pqkkkkk.my_day_server.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
public class StepRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private StepRepository stepRepository;

    // --------- Test cases for findAllStepsByTaskIds method ---------
    @Test
    public void findAllStepsByTaskIds_WithExistingTaskIds_ShouldReturnSteps() {
        // Given
        User user = UserTestDataBuilder.createTestUser();
        entityManager.persistAndFlush(user);

        Task mockTask1 = TaskDataBuidler.createValidTaskWithoutList(user);
        Step stepOfTask1 = StepTestDataBuilder.createValidStep();
        stepOfTask1.setTask(mockTask1);
        mockTask1.setSteps(List.of(stepOfTask1));
        entityManager.persistAndFlush(mockTask1);

        Task mockTask2 = TaskDataBuidler.createValidTaskWithoutList(user);
        Step stepOfTask2 = StepTestDataBuilder.createValidStep();
        stepOfTask2.setTask(mockTask2);
        mockTask2.setSteps(List.of(stepOfTask2));

        entityManager.persistAndFlush(mockTask2);



        // When
        // Call the findAllStepsByTaskIds method with valid task IDs
        List<Step> steps = stepRepository.findAllByTaskIds(List.of(mockTask1.getTaskId(), mockTask2.getTaskId()));
        // Then
        // Assert that the returned list of steps matches the expected steps

        assertEquals(2, steps.size());
    }
    @Test
    public void findAllStepsByTaskIds_WithOnlyOneTaskId_ShouldReturnOnlyStepsOfCorrespondingTask() {
        // Given
        User user = UserTestDataBuilder.createTestUser();
        entityManager.persistAndFlush(user);

        Task mockTask1 = TaskDataBuidler.createValidTaskWithoutList(user);
        Step stepOfTask1 = StepTestDataBuilder.createValidStep();
        stepOfTask1.setTask(mockTask1);
        mockTask1.setSteps(List.of(stepOfTask1));
        entityManager.persistAndFlush(mockTask1);

        Task mockTask2 = TaskDataBuidler.createValidTaskWithoutList(user);
        Step stepOfTask2 = StepTestDataBuilder.createValidStep();
        stepOfTask2.setTask(mockTask2);
        mockTask2.setSteps(List.of(stepOfTask2));

        entityManager.persistAndFlush(mockTask2);

        // When
        // Call the findAllStepsByTaskIds method with a single valid task ID
        List<Step> steps = stepRepository.findAllByTaskIds(List.of(mockTask1.getTaskId()));

        // Then
        // Assert that the returned list of steps matches the expected steps
        assertEquals(1, steps.size());
    }
    @Test
    public void findAllStepsByTaskIds_WithNoneExistingTaskIds_ShouldReturnEmptyList() {
        // Given
        // No tasks or steps are created
        User user = UserTestDataBuilder.createTestUser();
        entityManager.persistAndFlush(user);

        Task mockTask1 = TaskDataBuidler.createValidTaskWithoutList(user);
        Step stepOfTask1 = StepTestDataBuilder.createValidStep();
        stepOfTask1.setTask(mockTask1);
        mockTask1.setSteps(List.of(stepOfTask1));
        entityManager.persistAndFlush(mockTask1);

        Task mockTask2 = TaskDataBuidler.createValidTaskWithoutList(user);
        Step stepOfTask2 = StepTestDataBuilder.createValidStep();
        stepOfTask2.setTask(mockTask2);
        mockTask2.setSteps(List.of(stepOfTask2));

        entityManager.persistAndFlush(mockTask2);

        // When
        // Call the findAllStepsByTaskIds method with non-existing task IDs
        List<Step> steps = stepRepository.findAllByTaskIds(List.of(999L, 1000L));

        // Then
        // Assert that the returned list of steps is empty
        assertEquals(0, steps.size());
    }
}
