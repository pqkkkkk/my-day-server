package org.pqkkkkk.my_day_server.task.dao.list;

import org.pqkkkkk.my_day_server.task.entity.MyList;

public interface ListCommandDao {
    public MyList addList(MyList list);
    public MyList updateList(MyList list);
    public Integer deleteList(Long listId);

}
