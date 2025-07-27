package org.pqkkkkk.my_day_server.task.service.impl;

import org.pqkkkkk.my_day_server.task.Constants.TaskStatus;
import org.pqkkkkk.my_day_server.task.dao.task.TaskCommandDao;
import org.pqkkkkk.my_day_server.task.entity.Task;
import org.pqkkkkk.my_day_server.task.service.TaskService;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskCommandDao taskCommandDao;

    public TaskServiceImpl(TaskCommandDao taskCommandDao) {
        this.taskCommandDao = taskCommandDao;
    }

    @Override
    @Transactional
    public Task createTask(Task task) {
        task.setTaskStatus(TaskStatus.TODO); // Default status for new tasks

        return taskCommandDao.addTask(task);
    }
    
    @Override
    @Transactional
    public Task updateTask(Task task) {
        return taskCommandDao.updateTask(task);
    }

    @Override
    public Integer deleteTask(Long id) {
        return taskCommandDao.deleteTask(id);
    }
}
