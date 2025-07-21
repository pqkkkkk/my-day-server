package org.pqkkkkk.my_day_server.user.dao;

import java.util.Map;

import org.pqkkkkk.my_day_server.common.BaseDao;
import org.pqkkkkk.my_day_server.user.entity.User;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserPostgresDao extends BaseDao<User> implements UserDao {
    private final String tableName = "my_day_user_table";
    
    public UserPostgresDao(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, "my_day_user_table");
    }
    @Override
    public User addUser(User user) {
        Integer result = insert(user);

        if(result <= 0)
            return null;
        
        return user;
    }

    @Override
    public User updateUser(User user) {
        Integer result = update(user);

        if(result <= 0)
            return null;
        
        return user;
    }

    @Override
    public Integer deleteUser(Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }

    @Override
    public User getUserByUsername(String username) {
        String sql = """
                SELECT * FROM %s 
                WHERE username = :username
                """.formatted(tableName);
        Map<String, Object> params = Map.of("username", username);
        
        return queryForObject(sql, params, User.class);
    }

}
