package org.pqkkkkk.my_day_server.task.dao.jpa_repository;

import org.pqkkkkk.my_day_server.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

}
