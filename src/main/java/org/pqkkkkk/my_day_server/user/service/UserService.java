package org.pqkkkkk.my_day_server.user.service;

import org.pqkkkkk.my_day_server.user.entity.User;

public interface UserService {
    public User addUser(User user);

    /**
     * Fetches a user by their username.
     * Throws UserNotFoundException if the user is not found.
     * @param username
     * @return user
     * @throws UserNotFoundException
     */
    public User getUserByUsername(String username);
}
