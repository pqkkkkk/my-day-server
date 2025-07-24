package org.pqkkkkk.my_day_server.task.service.impl;

import org.pqkkkkk.my_day_server.task.dao.list.ListQueryDao;
import org.pqkkkkk.my_day_server.task.dto.DTO.MyListDTO;
import org.pqkkkkk.my_day_server.task.dto.FilterObject.ListFilterObject;
import org.pqkkkkk.my_day_server.task.service.ListAggregationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class ListAggregationServiceImpl implements ListAggregationService {
    private final ListQueryDao listQueryDao;

    public ListAggregationServiceImpl(ListQueryDao listQueryDao) {
        this.listQueryDao = listQueryDao;
    }
    @Override
    public Page<MyListDTO> getLists(ListFilterObject filterObject) {
        Direction sortDirection = filterObject.sortDirection() == null ? Direction.ASC : 
            filterObject.sortDirection().name().equalsIgnoreCase("DESC") ? Direction.DESC : Direction.ASC;

        Sort sort = Sort.by(sortDirection, filterObject.sortBy());

        Pageable pageable = PageRequest.of(
            filterObject.currentPage() - 1, // Chuyển từ 1-based sang 0-based cho Spring Data
            filterObject.pageSize(),
            sort
        );
        
        return listQueryDao.fetchListDTOs(filterObject, pageable);
    }

}
