package org.pqkkkkk.my_day_server.task.dao.list;


import java.util.Map;

import org.pqkkkkk.my_day_server.common.BaseDao;
import org.pqkkkkk.my_day_server.common.EntityNotFoundException;
import org.pqkkkkk.my_day_server.task.entity.MyList;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ListCommandSqlDao extends BaseDao<MyList> implements ListCommandDao {
    private final String TABLE_NAME = "my_day_list_db";

    public ListCommandSqlDao(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, "my_day_list_db");
    }

    @Override
    public MyList addList(MyList list) {
        Integer result = super.insert(list);

        if(result <= 0)
            return null;

        return list;
    }

    @Override
    public MyList updateList(MyList list) {
        Integer result = super.update(list);

        if(result <= 0)
            throw new EntityNotFoundException("List", list.getListId());

        return list;
    }

    @Override
    public Integer deleteList(Long listId) {
        String sql = """
                DELETE FROM %s
                WHERE listId = :listId
                """.formatted(TABLE_NAME);

        Integer result = jdbcTemplate.update(sql, Map.of("listId", listId));

        return result;
    }
}
