package org.pqkkkkk.my_day_server.task.service;

import org.pqkkkkk.my_day_server.task.entity.Step;

public interface StepService {
    public Step createStep(Step step);
    public Step updateStep(Step step);
    public Integer deleteStep(Long stepId);
}
