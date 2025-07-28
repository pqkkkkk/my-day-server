package org.pqkkkkk.my_day_server.task.dao.jpa_repository;

import java.util.List;

import org.pqkkkkk.my_day_server.task.entity.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StepRepository extends JpaRepository<Step, Long> {
    @Query("SELECT s FROM Step s WHERE s.task.taskId IN :taskIds")
    public List<Step> findAllByTaskIds(List<Long> taskIds);
}
