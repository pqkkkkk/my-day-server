package org.pqkkkkk.my_day_server.user.dao.jpa_repository;

import org.pqkkkkk.my_day_server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query(value = """
            SELECT u FROM User u WHERE u.username = :username
            """)
    public User findByUsername(String username);
}
