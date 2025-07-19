package org.pqkkkkk.my_day_server.service;

import org.pqkkkkk.my_day_server.entity.List;

public interface ListService {
    public List createList(List list);
    public List updateList(List list);
    public Integer deleteList(Long id);
}
