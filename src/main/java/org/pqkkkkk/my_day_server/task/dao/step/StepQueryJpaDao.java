package org.pqkkkkk.my_day_server.task.dao.step;

import java.util.List;

import org.pqkkkkk.my_day_server.task.dao.jpa_repository.StepRepository;
import org.pqkkkkk.my_day_server.task.entity.Step;
import org.springframework.stereotype.Repository;

@Repository
public class StepQueryJpaDao implements StepQueryDao {

    private final StepRepository stepRepository;

    public StepQueryJpaDao(StepRepository stepRepository) {
        this.stepRepository = stepRepository;
    }

    @Override
    public List<Step> findAllByTaskIds(List<Long> taskIds) {
        return stepRepository.findAllByTaskIds(taskIds);
    }

}
