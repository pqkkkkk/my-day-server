package org.pqkkkkk.my_day_server.task.dao.task;

import org.pqkkkkk.my_day_server.task.entity.Task;

public interface TaskCommandDao {
    public Task addTask(Task task);
    public Task updateTask(Task task);
    public Integer deleteTask(Long taskId);
}
