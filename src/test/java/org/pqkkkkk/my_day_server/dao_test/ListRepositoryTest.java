package org.pqkkkkk.my_day_server.dao_test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.pqkkkkk.my_day_server.data_builder.UserTestDataBuilder;
import org.pqkkkkk.my_day_server.task.Constants.ListCategory;
import org.pqkkkkk.my_day_server.task.dao.jpa_repository.ListRepository;
import org.pqkkkkk.my_day_server.task.dto.DTO.MyListDTO;
import org.pqkkkkk.my_day_server.task.dto.FilterObject.ListFilterObject;
import org.pqkkkkk.my_day_server.task.entity.MyList;
import org.pqkkkkk.my_day_server.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
public class ListRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private ListRepository listRepository;
    
    @Test
    void should_FindListsByCategory_When_CategoryExists() {
        // Given
        User user = UserTestDataBuilder.createTestUser();
        entityManager.persistAndFlush(user);  // Lưu user vào DB
        
        MyList list = MyList.builder()
            .listTitle("Work Tasks")
            .listCategory(ListCategory.WORK)
            .user(user)
            .build();
        entityManager.persistAndFlush(list);
        
        // When
        ListFilterObject filterObject = new ListFilterObject(1, 10,null, null, null,
        ListCategory.WORK, null, null);
        Page<MyListDTO> result = listRepository.fetchListDTOs(
            filterObject,
            PageRequest.of(0, 10)
        );
        
        // Then
        assertEquals(1, result.getTotalElements());
    }
}
