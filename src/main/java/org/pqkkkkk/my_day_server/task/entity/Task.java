package org.pqkkkkk.my_day_server.task.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.pqkkkkk.my_day_server.task.Constants.TaskPriority;
import org.pqkkkkk.my_day_server.task.Constants.TaskStatus;
import org.pqkkkkk.my_day_server.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "my_day_task_table")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @jakarta.persistence.Column(name = "task_title", nullable = false, length = 100)
    private String taskTitle;

    @jakarta.persistence.Column(name = "task_description", columnDefinition = "TEXT")
    private String taskDescription;

    @Min(0)
    private Integer estimatedTime;

    @Min(0)
    private Integer actualTime;

    private LocalDateTime deadline;

    @jakarta.persistence.Enumerated(EnumType.STRING)
    private TaskPriority taskPriority;

    @jakarta.persistence.Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    @jakarta.persistence.Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @jakarta.persistence.Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "list_id")
    private MyList list;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "task")
    private List<Step> steps;
}
