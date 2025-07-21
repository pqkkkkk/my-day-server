package org.pqkkkkk.my_day_server.user.service;

import org.pqkkkkk.my_day_server.user.dto.BusinessResult.SignInResult;
import org.pqkkkkk.my_day_server.user.entity.User;

public interface AuthService {
    public SignInResult signIn(String username, String password);
    public User signUp(User user);
}
