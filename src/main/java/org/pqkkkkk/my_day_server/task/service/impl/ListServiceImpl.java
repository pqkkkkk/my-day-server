package org.pqkkkkk.my_day_server.task.service.impl;

import org.pqkkkkk.my_day_server.task.dao.list.ListCommandDao;
import org.pqkkkkk.my_day_server.task.entity.MyList;
import org.pqkkkkk.my_day_server.task.service.ListService;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class ListServiceImpl implements ListService {
    private final ListCommandDao listCommandDao;

    public ListServiceImpl(ListCommandDao listCommandDao) {
        this.listCommandDao = listCommandDao;
    }
    @Override
    @Transactional
    public MyList createList(MyList list) {
        return listCommandDao.addList(list);
    }

    @Override
    public MyList updateList(MyList list) {
        MyList updatedList = listCommandDao.updateList(list);
        return updatedList;
    }

    @Override
    public Integer deleteList(Long id) {
        return listCommandDao.deleteList(id);
    }

}
