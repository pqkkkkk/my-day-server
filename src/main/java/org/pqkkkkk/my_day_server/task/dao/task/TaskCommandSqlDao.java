package org.pqkkkkk.my_day_server.task.dao.task;

import java.util.Map;

import org.pqkkkkk.my_day_server.common.BaseDao;
import org.pqkkkkk.my_day_server.common.EntityNotFoundException;
import org.pqkkkkk.my_day_server.task.entity.Task;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TaskCommandSqlDao extends BaseDao<Task> implements TaskCommandDao {
    private final String TABLE_NAME = "my_day_task_db";

    public TaskCommandSqlDao(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, "my_day_task_db");
    }

    @Override
    public Task addTask(Task task) {
        Integer result = super.insert(task);

        if(result <= 0)
            return null;

        return task;
    }

    @Override
    public Task updateTask(Task task) {
        Integer result = super.update(task);

        if(result <= 0)
            throw new EntityNotFoundException("Task", task.getTaskId());

        return task;
    }

    @Override
    public Integer deleteTask(Long taskId) {
        String sql = """
                DELETE FROM %s
                WHERE taskId = :taskId
                """.formatted(TABLE_NAME);

        Integer result = jdbcTemplate.update(sql, Map.of("taskId", taskId));

        return result;
    }
}
