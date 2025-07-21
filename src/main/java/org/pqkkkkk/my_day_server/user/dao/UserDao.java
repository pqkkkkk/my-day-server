package org.pqkkkkk.my_day_server.user.dao;

import org.pqkkkkk.my_day_server.user.entity.User;

public interface UserDao {
    User addUser(User user);
    User updateUser(User user);
    Integer deleteUser(Long userId);
    User getUserByUsername(String username);
}
