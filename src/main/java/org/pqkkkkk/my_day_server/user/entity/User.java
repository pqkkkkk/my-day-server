package org.pqkkkkk.my_day_server.user.entity;

import org.pqkkkkk.my_day_server.task.entity.Column;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class User {
    @Column(isPrimaryKey = true, insertable = true, updatable = false, value = "username")
    String username;

    @Column("user_password")
    String userPassword;

    @Column("user_email")
    String userEmail;

    @Column("user_full_name")
    String userFullName;
}
