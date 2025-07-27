package org.pqkkkkk.my_day_server.task.dao.task;

import org.pqkkkkk.my_day_server.task.dao.jpa_repository.TaskRepository;
import org.pqkkkkk.my_day_server.task.entity.Task;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class TaskCommandJpaDao implements TaskCommandDao {
    private final TaskRepository taskRepository;

    public TaskCommandJpaDao(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    @Override
    public Task addTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Integer deleteTask(Long taskId) {
        int deleteCount = taskRepository.deleteByTaskId(taskId);

        return deleteCount;
    }

}
