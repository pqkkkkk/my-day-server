package org.pqkkkkk.my_day_server.task.dao.step;

import org.pqkkkkk.my_day_server.task.entity.Step;

public interface StepCommandDao {
    public Step addStep(Step step);
    public Step updateStep(Step step);
    public Integer deleteStep(Long stepId);
}
