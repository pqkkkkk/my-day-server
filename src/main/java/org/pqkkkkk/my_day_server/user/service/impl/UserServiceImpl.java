package org.pqkkkkk.my_day_server.user.service.impl;

import org.pqkkkkk.my_day_server.user.dao.UserDao;
import org.pqkkkkk.my_day_server.user.entity.User;
import org.pqkkkkk.my_day_server.user.exception.UserNotFoundException;
import org.pqkkkkk.my_day_server.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User addUser(User user) {
        return userDao.addUser(user);
    }

    @Override
    public User getUserByUsername(String username) {
        User user = userDao.getUserByUsername(username);

        if(user == null){
            throw new UserNotFoundException("User not found with username: " + username);
        }
        
        return user;
    }

}
