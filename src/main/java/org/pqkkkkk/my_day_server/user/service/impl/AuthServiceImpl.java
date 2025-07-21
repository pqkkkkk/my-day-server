package org.pqkkkkk.my_day_server.user.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.pqkkkkk.my_day_server.user.dto.BusinessResult.SignInResult;
import org.pqkkkkk.my_day_server.user.entity.User;
import org.pqkkkkk.my_day_server.user.exception.ExistedUserException;
import org.pqkkkkk.my_day_server.user.exception.UserNotFoundException;
import org.pqkkkkk.my_day_server.user.exception.WrongPasswordException;
import org.pqkkkkk.my_day_server.user.service.AuthService;
import org.pqkkkkk.my_day_server.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;

@Service
public class AuthServiceImpl implements AuthService {
    @Value("${jwt.signer-key}")
    private String SIGNER_KEY;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public SignInResult signIn(String username, String password) {
        User user = userService.getUserByUsername(username);

        if(user == null)
        {
            throw new UserNotFoundException("User not found with username: " + username);
        }

        boolean authenticated = passwordEncoder.matches(password, user.getUserPassword());
        if(!authenticated) {
            throw new WrongPasswordException("Invalid password for user: " + username);
        }

        String accessToken = generateToken(user);

        return new SignInResult(accessToken, user, authenticated);
        
    }

    @Override
    public User signUp(User user) {
        boolean userExists = userService.getUserByUsername(user.getUsername()) != null;

        if(userExists) {
            throw new ExistedUserException("User already exists with username: " + user.getUsername());
        }

        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        
        User createdUser = userService.addUser(user);
        createdUser.setUserPassword(null);

        return createdUser;
    }

    private String generateToken(User user)
    {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                                .subject(user.getUsername())
                                .issuer("pqkiet854")
                                .issueTime(new Date())
                                .expirationTime(new Date(
                                                Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                                                ))
                                .claim("scope", buildScope(user))
                                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
    private String buildScope(User user)
    {
        return "USER";
    }

}
