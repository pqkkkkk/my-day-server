package org.pqkkkkk.my_day_server.task.service.impl;

import org.pqkkkkk.my_day_server.task.dao.list.ListQueryDao;
import org.pqkkkkk.my_day_server.task.dto.DTO.MyListDTO;
import org.pqkkkkk.my_day_server.task.dto.FilterObject.ListFilterObject;
import org.pqkkkkk.my_day_server.task.service.ListAggregationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ListAggregationServiceImpl implements ListAggregationService {
    private final ListQueryDao listQueryDao;

    public ListAggregationServiceImpl(ListQueryDao listQueryDao) {
        this.listQueryDao = listQueryDao;
    }
    @Override
    public Page<MyListDTO> getLists(ListFilterObject filterObject) {
        Pageable pageable = PageRequest.of(
            filterObject.currentPage(), 
            filterObject.pageSize()
        );
        
        return listQueryDao.fetchListDTOs(filterObject, pageable);
    }

}
