package org.pqkkkkk.my_day_server.task.dao.step;

import java.util.List;

import org.pqkkkkk.my_day_server.task.entity.Step;

public interface StepQueryDao {
    public List<Step> findAllByTaskIds(List<Long> taskIds);
}
