package org.pqkkkkk.my_day_server.task.service;

import org.pqkkkkk.my_day_server.task.entity.List;

public interface ListService {
    public List createList(List list);
    public List updateList(List list);
    public Integer deleteList(Long id);
}
