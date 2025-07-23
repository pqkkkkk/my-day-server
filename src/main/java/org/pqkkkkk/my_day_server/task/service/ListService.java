package org.pqkkkkk.my_day_server.task.service;

import org.pqkkkkk.my_day_server.task.entity.MyList;

public interface ListService {
    public MyList createList(MyList list);
    public MyList updateList(MyList list);
    public Integer deleteList(Long id);
}
