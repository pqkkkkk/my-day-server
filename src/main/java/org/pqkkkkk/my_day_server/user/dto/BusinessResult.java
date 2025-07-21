package org.pqkkkkk.my_day_server.user.dto;

import org.pqkkkkk.my_day_server.user.entity.User;

public class BusinessResult {
    public record SignInResult(
        String accessToken,
        String refreshToken,
        User user,
        boolean authenticated
    ){}
    public record RefreshTokenResult(
        String accessToken,
        String refreshToken
    ){}
}
