package org.pqkkkkk.my_day_server.user.api;

import jakarta.validation.constraints.NotBlank;

public class Request {
    public record SignInRequest(
        @NotBlank
        String username,

        @NotBlank
        String password) {
    }

    public record RefreshTokenRequest(
        @NotBlank
        String refreshToken) {
    }
}
