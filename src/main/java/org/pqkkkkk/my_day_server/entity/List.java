package org.pqkkkkk.my_day_server.entity;

import java.util.Date;

import org.pqkkkkk.my_day_server.Constants.ListCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class List {
    @Column(isPrimaryKey = true, insertable = false, updatable = false, value = "list_id")
    Long listId;

    @Column("list_title")
    String listTitle;

    @Column("list_description")
    String listDescription;

    @Column("list_category")
    ListCategory listCategory;

    @Column("color")
    String color;

    Date createdAt;
    Date updatedAt;
}
