package org.pqkkkkk.my_day_server.user.dto;

import org.pqkkkkk.my_day_server.user.entity.User;

public class BusinessResult {
    public record SignInResult(
        String accessToken,
        User user,
        boolean authenticated
    ){}
}
