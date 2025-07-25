package org.pqkkkkk.my_day_server.service_test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pqkkkkk.my_day_server.data_builder.ListDataBuilder;
import org.pqkkkkk.my_day_server.task.dao.list.ListCommandDao;
import org.pqkkkkk.my_day_server.task.entity.MyList;
import org.pqkkkkk.my_day_server.task.service.impl.ListServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ListServiceImplTest {
    @Mock
    private ListCommandDao listCommandDao;
    
    @InjectMocks
    private ListServiceImpl listService;
    
    @Test
    void should_CreateList_When_ValidListProvided() {
        // Given
        MyList inputList = ListDataBuilder.createTestList();
        MyList savedList = ListDataBuilder.createTestListWithId();
        when(listCommandDao.addList(inputList)).thenReturn(savedList);
        
        // When
        MyList result = listService.createList(inputList);
        
        // Then
        assertNotNull(result.getListId());
        verify(listCommandDao).addList(inputList);
    }
    
    // @Test
    // void should_ThrowException_When_ListNotFound() {
    //     // Given
    //     Long nonExistentId = 999L;
    //     when(listCommandDao.deleteList(nonExistentId)).thenReturn(0);
        
    //     // When & Then
    //     assertThrows(EntityNotFoundException.class, 
    //         () -> listService.deleteList(nonExistentId)
    //     );
    // }
}
