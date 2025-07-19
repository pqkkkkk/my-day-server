package org.pqkkkkk.my_day_server.task.service;


import org.pqkkkkk.my_day_server.task.entity.Task;

public interface TaskService {
    public Task createTask(Task task);
    public Task updateTask(Task task);
    public Integer deleteTask(Long id);

}
