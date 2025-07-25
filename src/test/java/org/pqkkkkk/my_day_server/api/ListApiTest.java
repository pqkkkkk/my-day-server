package org.pqkkkkk.my_day_server.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.pqkkkkk.my_day_server.data_builder.ListDataBuilder;
import org.pqkkkkk.my_day_server.task.Constants.ListCategory;
import org.pqkkkkk.my_day_server.task.api.ListApi;
import org.pqkkkkk.my_day_server.task.api.Request.CreateListRequest;
import org.pqkkkkk.my_day_server.task.entity.MyList;
import org.pqkkkkk.my_day_server.task.service.ListAggregationService;
import org.pqkkkkk.my_day_server.task.service.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(value = ListApi.class,
excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class ListApiTest {
     @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private ListService listService;
    
    @MockitoBean
    private ListAggregationService listAggregationService;
    
    @Test
    void should_CreateList_When_ValidRequestProvided() throws Exception {
        // Given
        CreateListRequest request = new CreateListRequest("Test List", "Description", ListCategory.PERSONAL, "#FFFFFF", "testuser");
        MyList createdList = ListDataBuilder.createTestListWithId();
        when(listService.createList(any(MyList.class))).thenReturn(createdList);
        
        // When & Then
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/list")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.listTitle").value("Test List"));
                               
    }
    
    @Test
    void should_ReturnBadRequest_When_InvalidRequestProvided() throws Exception {
        // Given - request with missing required fields
        CreateListRequest invalidRequest = new CreateListRequest("", "", ListCategory.PERSONAL, "#FFFFFF", "testuser");
        
        // When & Then
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/list")
                .content(new ObjectMapper().writeValueAsString(invalidRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
