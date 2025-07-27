package org.pqkkkkk.my_day_server.task.dao.list;

import org.pqkkkkk.my_day_server.common.EntityNotFoundException;
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
        MyList existingList = listRepository.findById(list.getListId())
            .orElse(null);
        
        if(existingList == null) {
            throw new EntityNotFoundException("List not found with id: " + list.getListId());
        }

        return listRepository.save(list);
    }

    @Override
    public Integer deleteList(Long listId) {
        MyList list = listRepository.findById(listId).orElse(null);

        if (list != null) {
            listRepository.delete(list); // JPA delete - cascade sẽ hoạt động
            return 1;
        }

        return 0;
    }

}
