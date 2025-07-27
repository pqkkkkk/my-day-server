package org.pqkkkkk.my_day_server.user.entity;

import java.util.List;

import org.pqkkkkk.my_day_server.task.entity.Column;
import org.pqkkkkk.my_day_server.task.entity.MyList;
import org.pqkkkkk.my_day_server.task.entity.Task;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "my_day_user_table")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class User {
    @Id
    @Column(isPrimaryKey = true, insertable = true, updatable = false, value = "username")
    @jakarta.persistence.Column(length = 50)
    @NotBlank
    String username;

    @Column("user_password")
    @jakarta.persistence.Column(name = "user_password", nullable = false, length = 100)
    @NotBlank
    String userPassword;

    @Column("user_email")
    @jakarta.persistence.Column(name = "user_email", nullable = false, unique = true, length = 100)
    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email format")
    String userEmail;

    @Column("user_full_name")
    @jakarta.persistence.Column(name = "user_full_name", nullable = false, length = 100)
    @NotBlank
    String userFullName;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<MyList> lists;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Task> tasks;
}
