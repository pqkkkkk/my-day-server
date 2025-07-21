package org.pqkkkkk.my_day_server.user.service.impl;


import org.pqkkkkk.my_day_server.user.dto.BusinessResult.RefreshTokenResult;
import org.pqkkkkk.my_day_server.user.dto.BusinessResult.SignInResult;
import org.pqkkkkk.my_day_server.user.entity.User;
import org.pqkkkkk.my_day_server.user.exception.ExistedUserException;
import org.pqkkkkk.my_day_server.user.exception.UserNotFoundException;
import org.pqkkkkk.my_day_server.user.exception.WrongPasswordException;
import org.pqkkkkk.my_day_server.user.service.AuthService;
import org.pqkkkkk.my_day_server.user.service.JwtUtils;
import org.pqkkkkk.my_day_server.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Value("${jwt.signer-key}")
    private String SIGNER_KEY;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthServiceImpl(UserService userService, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public SignInResult signIn(String username, String password) {
        User user = userService.getUserByUsername(username);

        validatePassword(password, user.getUserPassword());

        return createSignInResult(user);
    }
    private void validatePassword(String password, String encodedPassword) {
        boolean isValid = passwordEncoder.matches(password, encodedPassword);

        if (!isValid) {
            throw new WrongPasswordException("Invalid password format");
        }
    }
    private SignInResult createSignInResult(User user) {
        String accessToken = jwtUtils.generateAcessToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);
        user.setUserPassword(null); // Clear password before returning user object
        
        return new SignInResult(accessToken, refreshToken, user, true);
    }

    @Override
    public User signUp(User user) {
        checkUserExists(user.getUsername());

        user.setUserPassword(passwordEncoder.encode(user.getUserPassword())); // encode password
        
        User createdUser = userService.addUser(user);

        return prepareCreatedUser(createdUser);
    }
    private void checkUserExists(String username){
        User existingUser = userService.getUserByUsername(username);

        try{
            if(existingUser != null){
                throw new ExistedUserException("User already exists with username: " + username);
            }
        } catch (UserNotFoundException e) {
            // User does not exist, continue with sign up
        }
    }
    private User prepareCreatedUser(User user) {
        return User.builder()
                .username(user.getUsername())
                .userPassword(null)
                .userEmail(user.getUserEmail())
                .userFullName(user.getUserFullName())
                .build();
    }
    
    @Override
    public RefreshTokenResult refreshToken(String refreshToken) {
        validateRefreshToken(refreshToken);

        String username = jwtUtils.extractUsername(refreshToken);
        User user = userService.getUserByUsername(username);

        String newAccessToken = jwtUtils.generateAcessToken(user);

        return new RefreshTokenResult(newAccessToken, refreshToken);
    }
    /**
     * Validates the provided refresh token.
     * @param refreshToken
     * @throws InvalidBearerTokenException if the refresh token is invalid.
     */
    private void validateRefreshToken(String refreshToken) {
        boolean isValid = jwtUtils.validateToken(refreshToken);
        if (!isValid) {
            throw new InvalidBearerTokenException("Invalid refresh token: " + refreshToken);
        }
    }
}
