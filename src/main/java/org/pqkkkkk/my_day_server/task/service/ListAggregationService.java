package org.pqkkkkk.my_day_server.task.service;

import org.pqkkkkk.my_day_server.task.dto.DTO.MyListDTO;
import org.pqkkkkk.my_day_server.task.dto.FilterObject.ListFilterObject;
import org.springframework.data.domain.Page;

public interface ListAggregationService {
    public Page<MyListDTO> getLists(ListFilterObject filterObject);
}
