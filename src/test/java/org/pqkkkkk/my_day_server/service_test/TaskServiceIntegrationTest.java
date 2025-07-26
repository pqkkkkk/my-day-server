package org.pqkkkkk.my_day_server.service_test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.pqkkkkk.my_day_server.data_builder.ListDataBuilder;
import org.pqkkkkk.my_day_server.data_builder.TaskDataBuidler;
import org.pqkkkkk.my_day_server.data_builder.UserTestDataBuilder;
import org.pqkkkkk.my_day_server.task.entity.MyList;
import org.pqkkkkk.my_day_server.task.entity.Task;
import org.pqkkkkk.my_day_server.task.service.TaskService;
import org.pqkkkkk.my_day_server.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TaskServiceIntegrationTest {
    @Autowired
    private TaskService taskService;
    @Autowired
    private EntityManager entityManager;

    @Test
    public void createTask_WithValidTaskInList_ShouldCreateTask(){
        // Given
        // Create a valid task request object
        User user = UserTestDataBuilder.createTestUser();
        entityManager.persist(user);
        MyList myList = ListDataBuilder.createTestList();
        entityManager.persist(myList);

        Task task = TaskDataBuidler.createValidTaskWithList(myList);

        // When
        Task createdTask = taskService.createTask(task);

        // Then
        assertNotNull(createdTask);
        assertNotNull(createdTask.getTaskId());
        assertNull(createdTask.getUser());
    }

    @Test
    public void createTask_WithValidTaskNotInList_ShouldCreateTask(){
        // Given
        // Create a valid task request object
        User user = UserTestDataBuilder.createTestUser();
        entityManager.persist(user);

        Task task = TaskDataBuidler.createValidTaskWithoutList(user);

        // When
        Task createdTask = taskService.createTask(task);

        // Then
        assertNotNull(createdTask);
        assertNotNull(createdTask.getTaskId());
        assertNotNull(createdTask.getUser());
        assertNull(createdTask.getList());
    }
}
