package org.pqkkkkk.my_day_server.task.api;

import org.pqkkkkk.my_day_server.common.ApiResponse;
import org.pqkkkkk.my_day_server.task.api.Request.CreateListRequest;
import org.pqkkkkk.my_day_server.task.dto.DTO.MyListDTO;
import org.pqkkkkk.my_day_server.task.dto.FilterObject.ListFilterObject;
import org.pqkkkkk.my_day_server.task.entity.MyList;
import org.pqkkkkk.my_day_server.task.service.ListAggregationService;
import org.pqkkkkk.my_day_server.task.service.ListService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/list")
public class ListApi {
    private final ListService listService;
    private final ListAggregationService listAggregationService;

    public ListApi(ListService listService, ListAggregationService listAggregationService) {
        this.listService = listService;
        this.listAggregationService = listAggregationService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MyListDTO>> createList(@Valid @RequestBody CreateListRequest request) {
        MyList list = CreateListRequest.toEntity(request);

        MyList createdList = listService.createList(list);
        MyListDTO listDTO = MyListDTO.from(createdList);

        ApiResponse<MyListDTO> response = new ApiResponse<>(listDTO, true,
                                HttpStatus.CREATED.value(),
                                "List created successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MyList>> updateList(@PathVariable Long id,
                                     @Valid @RequestBody MyList list) {
        list.setListId(id);
        MyList updatedList = listService.updateList(list);
        ApiResponse<MyList> response = new ApiResponse<>(updatedList, true,
                                HttpStatus.OK.value(),
                                "List updated successfully");

        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteList(@PathVariable Long id) {
        Integer deleteResult = listService.deleteList(id);
        boolean isDeleted = deleteResult > 0;
        ApiResponse<Void> response = new ApiResponse<>(null, isDeleted,
                                isDeleted ?  HttpStatus.NO_CONTENT.value() : HttpStatus.NOT_FOUND.value(),
                                isDeleted ? "List deleted successfully" : "List not found");

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping
    public ResponseEntity<ApiResponse<Page<MyListDTO>>> getAllLists(
        @Valid @ModelAttribute ListFilterObject listFilterObject
    ) {
        Page<MyListDTO> lists = listAggregationService.getLists(listFilterObject);

        ApiResponse<Page<MyListDTO>> response = new ApiResponse<>(lists, true,
                                HttpStatus.OK.value(),
                                "Lists retrieved successfully");

        return ResponseEntity.ok(response);
    }

}
