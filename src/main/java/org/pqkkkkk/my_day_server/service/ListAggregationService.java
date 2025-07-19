package org.pqkkkkk.my_day_server.service;

import java.util.List;

import org.pqkkkkk.my_day_server.dto.ListAggregation;
import org.pqkkkkk.my_day_server.dto.FilterObject.ListFilterObject;

public interface ListAggregationService {
    public List<ListAggregation> getLists(ListFilterObject filterObject);
    public List<ListAggregation> getListWithTasks(ListFilterObject filterObject);
}
