package org.pqkkkkk.my_day_server.user.api;

import org.pqkkkkk.my_day_server.common.ApiResponse;
import org.pqkkkkk.my_day_server.user.api.Request.SignInRequest;
import org.pqkkkkk.my_day_server.user.dto.BusinessResult.SignInResult;
import org.pqkkkkk.my_day_server.user.entity.User;
import org.pqkkkkk.my_day_server.user.service.AuthService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/v1/auth")
public class AuthApi {
    private final AuthService authService;

    public AuthApi(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<SignInResult>> login(@RequestBody SignInRequest request) {
        SignInResult result = authService.signIn(request.username(), request.password());
        ApiResponse<SignInResult> response = new ApiResponse<>(result, true, HttpStatus.OK.value(), "Sign in successful");
        return ResponseEntity.ok(response);
    }
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<User>> signUp(@RequestBody User user) {
        User createdUser = authService.signUp(user);
        ApiResponse<User> response = new ApiResponse<>(createdUser, true, HttpStatus.CREATED.value(), "User created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
}
