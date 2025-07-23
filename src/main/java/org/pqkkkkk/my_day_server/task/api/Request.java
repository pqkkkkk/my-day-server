package org.pqkkkkk.my_day_server.task.api;

import org.pqkkkkk.my_day_server.task.Constants.ListCategory;
import org.pqkkkkk.my_day_server.task.entity.MyList;
import org.pqkkkkk.my_day_server.user.entity.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Request {
    public record CreateListRequest(

        @NotBlank
        @Size(max = 100)
        String listTitle,

        @NotBlank
        String listDescription,

        ListCategory listCategory,

        @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")
        String color,

        @NotBlank
        String username
    ) {
        public static MyList toEntity(CreateListRequest request) {
            User user = new User();
            user.setUsername(request.username);
            return MyList.builder()
                .listTitle(request.listTitle)
                .listDescription(request.listDescription)
                .listCategory(request.listCategory)
                .color(request.color)
                .user(user)
                .build();
        }
    }
}
