package org.pqkkkkk.my_day_server.task.dao.list;

import org.pqkkkkk.my_day_server.task.dao.jpa_repository.ListRepository;
import org.pqkkkkk.my_day_server.task.dto.DTO.MyListDTO;
import org.pqkkkkk.my_day_server.task.dto.FilterObject.ListFilterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class ListQueryJpaDao implements ListQueryDao {
    private final ListRepository listRepository;

    public ListQueryJpaDao(ListRepository listRepository) {
        this.listRepository = listRepository;
    }
    @Override
    public Page<MyListDTO> fetchListDTOs(ListFilterObject filterObject, Pageable pageable) {
        return listRepository.fetchListDTOs(filterObject, pageable);
    }
    
}
