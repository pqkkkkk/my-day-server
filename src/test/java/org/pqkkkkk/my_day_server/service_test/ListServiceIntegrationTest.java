package org.pqkkkkk.my_day_server.service_test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import org.junit.jupiter.api.Test;
import org.pqkkkkk.my_day_server.TestSecurityConfig;
import org.pqkkkkk.my_day_server.common.EntityNotFoundException;
import org.pqkkkkk.my_day_server.data_builder.ListDataBuilder;
import org.pqkkkkk.my_day_server.data_builder.TaskDataBuidler;
import org.pqkkkkk.my_day_server.data_builder.UserTestDataBuilder;
import org.pqkkkkk.my_day_server.task.entity.MyList;
import org.pqkkkkk.my_day_server.task.entity.Task;
import org.pqkkkkk.my_day_server.task.service.ListService;
import org.pqkkkkk.my_day_server.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@Import(TestSecurityConfig.class) // Import security configuration if needed
@ActiveProfiles("test")  // Only applies to this class
public class ListServiceIntegrationTest {
    @Autowired
    private ListService listService;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Test
    void should_CreateAndRetrieveList_Successfully() {
        // Given
        User user = UserTestDataBuilder.createTestUser();
        entityManager.persist(user);
        MyList list = MyList.builder()
            .listTitle("Integration Test List")
            .user(user)
            .build();
        
        // When
        MyList createdList = listService.createList(list);
        
        // Then
        assertNotNull(createdList.getListId());
        // Verify list exists in database
        MyList foundList = entityManager.find(MyList.class, createdList.getListId());
        assertNotNull(foundList);
    }

    // ----------- Test cases for delete operation -----------
    @Test
    public void deleteList_WithValidListWithoutTasks_ShouldDeleteList(){
        // Given
        User user = UserTestDataBuilder.createTestUser();
        entityManager.persist(user);

        MyList list = ListDataBuilder.createTestList();
        entityManager.persist(list);

        // When
        listService.deleteList(list.getListId());

        // Flush and clear to sync with database state
        entityManager.flush();
        entityManager.clear();

        // Then
        MyList deletedList = entityManager.find(MyList.class, list.getListId());
        assertNull(deletedList, "List should be deleted");
    }
    @Test
    public void deleteList_WithValidListWithTasks_ShouldDeleteBothListAndTasks(){
        // Given
        User user = UserTestDataBuilder.createTestUser();
        entityManager.persist(user);

        MyList list = ListDataBuilder.createTestListWithoutUser();
        list.setUser(user); // Use the same persisted user
        entityManager.persist(list);
        entityManager.flush(); // Ensure the list is persisted before adding tasks

        Task task = TaskDataBuidler.createValidTaskWithList(list);
        // SYNC BIDIRECTIONAL RELATIONSHIP
        if (list.getTasks() == null) {
            list.setTasks(new java.util.ArrayList<>());
        }
        list.getTasks().add(task); // Add task to list's collection
        
        entityManager.persist(task);

        // When
        listService.deleteList(list.getListId());


        // Then
        MyList deletedList = entityManager.find(MyList.class, list.getListId());
        assertNull(deletedList, "List should be deleted");

        // Verify tasks are also deleted
        Task deletedTask = entityManager.find(Task.class, task.getTaskId());
        assertNull(deletedTask, "Task should be deleted");
    }
    
    // --------- Test cases for update operation -----------
    @Test
    public void updateList_WithValidList_ShouldUpdateList() {
        // Given
        User user = UserTestDataBuilder.createTestUser();
        entityManager.persist(user);

        MyList list = ListDataBuilder.createTestListWithoutUser();
        list.setUser(user); // Use the same persisted user
        entityManager.persist(list);

        // When
        list.setListTitle("Updated List Title");
        MyList updatedList = listService.updateList(list);

        // Then
        assertNotNull(updatedList);
        assertNotNull(updatedList.getListId());
        assertNotNull(updatedList.getUser());
        assertEquals("Updated List Title", updatedList.getListTitle());
    }
    @Test
    public void updateList_WithNotExistingList_ShouldThrowEntityNotFoundException() {
        // Given
        MyList list = ListDataBuilder.createTestListWithoutUser();
        list.setListId(999L); // Non-existing ID

        // When & Then
        assertThrowsExactly(EntityNotFoundException.class, () -> listService.updateList(list));
    }
}


