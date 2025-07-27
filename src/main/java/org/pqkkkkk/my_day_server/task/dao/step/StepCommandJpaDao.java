package org.pqkkkkk.my_day_server.task.dao.step;

import org.pqkkkkk.my_day_server.common.EntityNotFoundException;
import org.pqkkkkk.my_day_server.task.dao.jpa_repository.StepRepository;
import org.pqkkkkk.my_day_server.task.entity.Step;
import org.springframework.stereotype.Repository;


@Repository
public class StepCommandJpaDao implements StepCommandDao {

    private final StepRepository stepRepository;

    public StepCommandJpaDao(StepRepository stepRepository) {
        this.stepRepository = stepRepository;
    }

    @Override
    public Step addStep(Step step) {
        return stepRepository.save(step);
    }

    @Override
    public Step updateStep(Step step) {
        stepRepository.findById(step.getStepId())
            .orElseThrow(() -> new EntityNotFoundException("Step not found with ID: " + step.getStepId()));
        
        return stepRepository.save(step);
    }

    @Override
    public Integer deleteStep(Long stepId) {
        Step step = stepRepository.findById(stepId)
            .orElseThrow(() -> new EntityNotFoundException("Step not found with ID: " + stepId));

        stepRepository.delete(step);
        return 1;
    }

}
