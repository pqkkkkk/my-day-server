package org.pqkkkkk.my_day_server.user.api;

import org.pqkkkkk.my_day_server.user.entity.User;

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

    public record SignUpRequest(
        @NotBlank
        String username,

        @NotBlank
        String userEmail,

        @NotBlank
        String userFullName,

        @NotBlank
        String userPassword) {
        
        public User toEntity(){
            return User.builder()
                .username(this.username)
                .userEmail(this.userEmail)
                .userFullName(this.userFullName)
                .userPassword(this.userPassword)
                .build();
        }
    }
}
