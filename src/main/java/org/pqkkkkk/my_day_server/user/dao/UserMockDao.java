package org.pqkkkkk.my_day_server.user.dao;

import org.pqkkkkk.my_day_server.user.entity.User;
import org.springframework.stereotype.Service;

@Service("userMockDao")
public class UserMockDao implements UserDao {

    @Override
    public User addUser(User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addUser'");
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
        return null;
    }

}
