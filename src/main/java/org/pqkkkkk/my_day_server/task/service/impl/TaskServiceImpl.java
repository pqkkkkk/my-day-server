package org.pqkkkkk.my_day_server.task.service.impl;

import org.pqkkkkk.my_day_server.task.dao.task.TaskCommandDao;
import org.pqkkkkk.my_day_server.task.entity.Task;
import org.pqkkkkk.my_day_server.task.service.TaskService;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskCommandDao taskCommandDao;

    public TaskServiceImpl(TaskCommandDao taskCommandDao) {
        this.taskCommandDao = taskCommandDao;
    }

    @Override
    public Task createTask(Task task) {
        return taskCommandDao.addTask(task);
    }

    @Override
    public Task updateTask(Task task) {
        return taskCommandDao.updateTask(task);
    }

    @Override
    public Integer deleteTask(Long id) {
        return taskCommandDao.deleteTask(id);
    }
}
