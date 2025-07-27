package org.pqkkkkk.my_day_server.task.service.impl;

import org.pqkkkkk.my_day_server.task.dao.step.StepCommandDao;
import org.pqkkkkk.my_day_server.task.entity.Step;
import org.pqkkkkk.my_day_server.task.service.StepService;
import org.springframework.stereotype.Service;

@Service
public class StepServiceImpl implements StepService {
    private final StepCommandDao stepCommandDao;

    public StepServiceImpl(StepCommandDao stepCommandDao) {
        this.stepCommandDao = stepCommandDao;
    }
    @Override
    public Step createStep(Step step) {
        return stepCommandDao.addStep(step);
    }

    @Override
    public Step updateStep(Step step) {
        return stepCommandDao.updateStep(step);
    }

    @Override
    public Integer deleteStep(Long stepId) {
        return stepCommandDao.deleteStep(stepId);
    }

}
