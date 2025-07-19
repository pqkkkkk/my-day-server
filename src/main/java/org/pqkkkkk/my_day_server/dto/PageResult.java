package org.pqkkkkk.my_day_server.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Represents a paginated result.
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class PageResult<T> {
    List<T> items;
    int totalCount;
    int currentPage;
    int pageSize;
}
