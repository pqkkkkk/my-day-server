package org.pqkkkkk.my_day_server.user.service;

import org.pqkkkkk.my_day_server.user.entity.User;

public interface UserService {
    public User addUser(User user);
    public User getUserByUsername(String username);
}
