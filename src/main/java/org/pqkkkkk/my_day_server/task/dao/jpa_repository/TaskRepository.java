package org.pqkkkkk.my_day_server.task.dao.jpa_repository;

import org.pqkkkkk.my_day_server.task.dto.DTO.TaskDTO;
import org.pqkkkkk.my_day_server.task.dto.FilterObject.TaskFilterObject;
import org.pqkkkkk.my_day_server.task.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Modifying
    @Query("DELETE FROM Task t WHERE t.taskId = :taskId")
    public int deleteByTaskId(Long taskId);

    @Query(value = """
            SELECT new org.pqkkkkk.my_day_server.task.dto.DTO$TaskDTO(
                t.taskId, t.taskTitle, t.taskDescription,
                t.estimatedTime, t.actualTime, CAST(t.deadline AS string),
                CAST(t.createdAt AS string), CAST(t.updatedAt AS string),
                CAST(t.taskPriority AS string), CAST(t.taskStatus AS string), CAST(t.list.listId AS long), t.user.username,
                CAST(COALESCE((SUM(CASE WHEN s.completed = true THEN 1 ELSE 0 END)), 0) AS integer),
                CAST(COALESCE(COUNT(s.stepId), 0) AS integer)
            )
            FROM Task t LEFT JOIN t.steps s
            WHERE (:#{#filterObject.listId} IS NULL OR t.list.listId = :#{#filterObject.listId})
                AND (:#{#filterObject.taskTitle} IS NULL OR LOWER(t.taskTitle) LIKE LOWER(CONCAT('%', :#{#filterObject.taskTitle}, '%')))
                AND (:#{#filterObject.taskStatus} IS NULL OR t.taskStatus = :#{#filterObject.taskStatus})
                AND (:#{#filterObject.taskPriority} IS NULL OR t.taskPriority = :#{#filterObject.taskPriority})
                AND (t.createdAt >= COALESCE(:#{#filterObject.createdAtFrom}, t.createdAt))
                AND (t.createdAt <= COALESCE(:#{#filterObject.createdAtTo}, t.createdAt))
                AND (:#{#filterObject.username} IS NULL OR t.user.username = :#{#filterObject.username})
            GROUP BY t.taskId, t.taskTitle, t.taskDescription, t.taskStatus,
                t.estimatedTime, t.actualTime, t.deadline, t.createdAt, t.updatedAt,
                t.taskPriority, t.list.listId, t.user.username
            """,
        countQuery = """
            SELECT COUNT(DISTINCT t.taskId)
            FROM Task t
            WHERE (:#{#filterObject.listId} IS NULL OR t.list.listId = :#{#filterObject.listId})
                AND (:#{#filterObject.taskTitle} IS NULL OR LOWER(t.taskTitle) LIKE LOWER(CONCAT('%', :#{#filterObject.taskTitle}, '%')))
                AND (:#{#filterObject.taskStatus} IS NULL OR t.taskStatus = :#{#filterObject.taskStatus})
                AND (:#{#filterObject.taskPriority} IS NULL OR t.taskPriority = :#{#filterObject.taskPriority})
                AND (t.createdAt >= COALESCE(:#{#filterObject.createdAtFrom}, t.createdAt))
                AND (t.createdAt <= COALESCE(:#{#filterObject.createdAtTo}, t.createdAt))
                AND (:#{#filterObject.username} IS NULL OR t.user.username = :#{#filterObject.username})
                """
        )
    public Page<TaskDTO> fetchTaskDTOs(TaskFilterObject filterObject,
                                    Pageable pageable);
}
