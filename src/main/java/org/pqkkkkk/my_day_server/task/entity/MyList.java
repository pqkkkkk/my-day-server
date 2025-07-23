package org.pqkkkkk.my_day_server.task.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.pqkkkkk.my_day_server.task.Constants.ListCategory;
import org.pqkkkkk.my_day_server.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "my_day_list_table")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class MyList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long listId;

    @jakarta.persistence.Column(name = "list_title", nullable = false, unique = true, length = 100)
    private String listTitle;

    @jakarta.persistence.Column(name = "list_description", columnDefinition = "TEXT")
    private String listDescription;

    @jakarta.persistence.Column(name = "list_category")
    @Enumerated(EnumType.STRING)
    private ListCategory listCategory;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")
    @jakarta.persistence.Column(length = 7)
    private String color;

    @jakarta.persistence.Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @jakarta.persistence.Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "list")
    private List<Task> tasks;
}
