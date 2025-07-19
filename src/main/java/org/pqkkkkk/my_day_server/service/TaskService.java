package org.pqkkkkk.my_day_server.service;


import org.pqkkkkk.my_day_server.entity.Task;

public interface TaskService {
    public Task createTask(Task task);
    public Task updateTask(Task task);
    public Integer deleteTask(Long id);

}
