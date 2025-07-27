package org.pqkkkkk.my_day_server.service_test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.pqkkkkk.my_day_server.TestSecurityConfig;
import org.pqkkkkk.my_day_server.data_builder.UserTestDataBuilder;
import org.pqkkkkk.my_day_server.task.entity.MyList;
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
}
