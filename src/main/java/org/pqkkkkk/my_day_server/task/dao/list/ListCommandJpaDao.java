package org.pqkkkkk.my_day_server.task.dao.list;

import org.pqkkkkk.my_day_server.task.dao.jpa_repository.ListRepository;
import org.pqkkkkk.my_day_server.task.entity.MyList;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class ListCommandJpaDao implements ListCommandDao {
    private final ListRepository listRepository;

    public ListCommandJpaDao(ListRepository listRepository) {
        this.listRepository = listRepository;
    }
    @Override
    public MyList addList(MyList list) {
        return listRepository.save(list);
    }

    @Override
    public MyList updateList(MyList list) {
        return listRepository.save(list);
    }

    @Override
    public Integer deleteList(Long listId) {
        listRepository.deleteById(listId);
        
        return 1;
    }

}
