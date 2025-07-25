package org.pqkkkkk.my_day_server.data_builder;

import org.pqkkkkk.my_day_server.task.Constants.ListCategory;
import org.pqkkkkk.my_day_server.task.dto.FilterObject.ListFilterObject;
import org.pqkkkkk.my_day_server.task.entity.MyList;

public class ListDataBuilder {
    public static MyList createTestList() {
        return MyList.builder()
                .listTitle("Test List")
                .listDescription("Test Description")
                .listCategory(ListCategory.PERSONAL)
                .color("#FF0000")
                .user(UserTestDataBuilder.createTestUser())
                .build();
    }
    public static MyList createTestListWithId() {
        return MyList.builder()
                .listId(1L)
                .listTitle("Test List")
                .listDescription("Test Description")
                .listCategory(ListCategory.PERSONAL)
                .color("#FF0000")
                .user(UserTestDataBuilder.createTestUser())
                .build();
    }
    public static ListFilterObject createTestListFilterObject() {
        return new ListFilterObject(1, 10,null, null, null,
        ListCategory.WORK, null, null);
    }
}
