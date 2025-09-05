package org.pqkkkkk.my_day_server.user.dto;

import org.pqkkkkk.my_day_server.user.dto.DTO.UserDTO;

public class BusinessResult {
    public record SignInResult(
        String accessToken,
        String refreshToken,
        UserDTO user,
        boolean authenticated
    ){}
    public record RefreshTokenResult(
        String accessToken,
        String refreshToken
    ){}
}
