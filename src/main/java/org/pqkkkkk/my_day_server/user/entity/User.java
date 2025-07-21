package org.pqkkkkk.my_day_server.user.entity;

import org.pqkkkkk.my_day_server.task.entity.Column;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @NotBlank
    String username;

    @Column("user_password")
    @NotBlank
    String userPassword;

    @Column("user_email")
    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email format")
    String userEmail;

    @Column("user_full_name")
    @NotBlank
    String userFullName;
}
