package org.pqkkkkk.my_day_server.task.service;

import java.util.List;

import org.pqkkkkk.my_day_server.task.dto.ListAggregation;
import org.pqkkkkk.my_day_server.task.dto.FilterObject.ListFilterObject;

public interface ListAggregationService {
    public List<ListAggregation> getLists(ListFilterObject filterObject);
    public List<ListAggregation> getListWithTasks(ListFilterObject filterObject);
}
