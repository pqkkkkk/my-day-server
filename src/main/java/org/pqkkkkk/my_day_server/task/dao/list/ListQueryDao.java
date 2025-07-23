package org.pqkkkkk.my_day_server.task.dao.list;

import org.pqkkkkk.my_day_server.task.dto.DTO.MyListDTO;
import org.pqkkkkk.my_day_server.task.dto.FilterObject.ListFilterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ListQueryDao {
    public Page<MyListDTO> fetchListDTOs(ListFilterObject filterObject, Pageable pageable);
}
