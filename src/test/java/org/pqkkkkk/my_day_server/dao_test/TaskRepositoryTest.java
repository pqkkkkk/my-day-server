package org.pqkkkkk.my_day_server.dao_test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.pqkkkkk.my_day_server.data_builder.ListDataBuilder;
import org.pqkkkkk.my_day_server.data_builder.TaskDataBuidler;
import org.pqkkkkk.my_day_server.data_builder.UserTestDataBuilder;
import org.pqkkkkk.my_day_server.task.dao.jpa_repository.TaskRepository;
import org.pqkkkkk.my_day_server.task.dto.DTO.TaskDTO;
import org.pqkkkkk.my_day_server.task.dto.FilterObject.TaskFilterObject;
import org.pqkkkkk.my_day_server.task.entity.MyList;
import org.pqkkkkk.my_day_server.task.entity.Task;
import org.pqkkkkk.my_day_server.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
public class TaskRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void fetchTaskDTOs_WithValidFilterObject_ShouldReturnTasks() {
        // Given
        // Create and persist a user, list, and tasks as needed for the test
        User mockUser = UserTestDataBuilder.createTestUser();
        entityManager.persistAndFlush(mockUser);
        
        MyList mockList = ListDataBuilder.createTestList();
        mockList.setUser(mockUser);
        entityManager.persistAndFlush(mockList);
        
        List<Task> mockTasks = TaskDataBuidler.getTasks(mockList, mockUser);
        for (Task task : mockTasks) {
            entityManager.persistAndFlush(task);
        }

        TaskFilterObject filterObject = TaskDataBuidler.getTaskFilterObject();

        // When
        // Call the fetchTaskDTOs method with a valid TaskFilterObject
        Page<TaskDTO> result = taskRepository.fetchTaskDTOs(filterObject,
                PageRequest.of(0, 10));

        // Then
        // Assert that the returned Page<TaskDTO> contains the expected results
        assertEquals(1, result.getTotalElements());

    }
}
