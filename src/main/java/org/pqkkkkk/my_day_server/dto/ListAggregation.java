package org.pqkkkkk.my_day_server.dto;

import java.util.Collections;

import org.pqkkkkk.my_day_server.entity.List;

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
public class ListAggregation {
    List list;

    @Builder.Default
    java.util.List<TaskAggregation> tasks = Collections.emptyList();
}
