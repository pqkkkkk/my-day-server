package org.pqkkkkk.my_day_server.user.dao;

import org.pqkkkkk.my_day_server.user.dao.jpa_repository.UserRepository;
import org.pqkkkkk.my_day_server.user.entity.User;
import org.springframework.stereotype.Repository;

@Repository("userJpaDao")
public class UserJpaDao implements UserDao {

    private final UserRepository userRepository;

    public UserJpaDao(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public Integer deleteUser(Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
