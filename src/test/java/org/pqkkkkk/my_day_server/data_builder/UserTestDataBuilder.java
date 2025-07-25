package org.pqkkkkk.my_day_server.data_builder;

import org.pqkkkkk.my_day_server.user.entity.User;

public class UserTestDataBuilder {
    public static User createTestUser() {
        return User.builder()
                .username("testuser")
                .userEmail("test@example.com")
                .userPassword("password123")
                .userFullName("Test User")
                .build();
    }
}
