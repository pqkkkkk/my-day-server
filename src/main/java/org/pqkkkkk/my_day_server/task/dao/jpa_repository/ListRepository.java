package org.pqkkkkk.my_day_server.task.dao.jpa_repository;


import org.pqkkkkk.my_day_server.task.dto.DTO.MyListDTO;
import org.pqkkkkk.my_day_server.task.dto.FilterObject.ListFilterObject;
import org.pqkkkkk.my_day_server.task.entity.MyList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ListRepository extends JpaRepository<MyList, Long> {

    @Query(value = """
        SELECT new org.pqkkkkk.my_day_server.task.dto.DTO$MyListDTO(
            l.listId, l.listTitle, l.listDescription, CAST(l.listCategory AS string), l.color,
            CAST(l.createdAt AS string), CAST(l.updatedAt AS string), l.user.username,
            CAST(COALESCE(SUM(CASE WHEN t.taskStatus = 'DONE' THEN 1 ELSE 0 END), 0) AS integer),
            CAST(COALESCE(COUNT(t.taskId), 0) AS integer)
        )
        FROM MyList l LEFT JOIN l.tasks t
        WHERE (:#{#filterObject.listCategory} IS NULL OR l.listCategory = :#{#filterObject.listCategory})
            AND (:#{#filterObject.listTitle} IS NULL OR LOWER(l.listTitle) LIKE LOWER(CONCAT('%', :#{#filterObject.listTitle}, '%')))
            AND (l.createdAt >= COALESCE(:#{#filterObject.createdAtFrom}, l.createdAt))
AND (l.createdAt <= COALESCE(:#{#filterObject.createdAtTo}, l.createdAt))
        GROUP BY l.listId, l.listTitle, l.listDescription, l.listCategory, l.color,
            l.createdAt, l.updatedAt, l.user.username
        """,
        countQuery = """
            SELECT COUNT(DISTINCT l.listId)
            FROM MyList l
            WHERE (:#{#filterObject.listCategory} IS NULL OR l.listCategory = :#{#filterObject.listCategory})
                    AND (:#{#filterObject.listTitle} IS NULL OR LOWER(l.listTitle) LIKE LOWER(CONCAT('%', :#{#filterObject.listTitle}, '%')))
                    AND (l.createdAt >= COALESCE(:#{#filterObject.createdAtFrom}, l.createdAt))
                    AND (l.createdAt <= COALESCE(:#{#filterObject.createdAtTo}, l.createdAt))
            """
    )

    public Page<MyListDTO> fetchListDTOs(ListFilterObject filterObject,
                                    Pageable pageable);

    @Modifying
    @Query("DELETE FROM MyList l WHERE l.listId = :listId")
    public int deleteByListId(Long listId);
}
