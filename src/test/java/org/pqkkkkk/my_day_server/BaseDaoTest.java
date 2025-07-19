package org.pqkkkkk.my_day_server;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pqkkkkk.my_day_server.common.BaseDao;
import org.pqkkkkk.my_day_server.task.entity.Column;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BaseDaoTest {

    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;

    private TestBaseDao testBaseDao;

    // Test entity class
    static class TestEntity {
        @Column(value = "user_id", isPrimaryKey = true)
        private Long id;
        
        @Column("user_name")
        private String name;
        
        @Column(value = "user_email", insertable = true)
        private String email;
        
        @Column(value = "created_at", insertable = false)
        private String createdAt;
        
        // Constructor
        public TestEntity() {}
        
        public TestEntity(Long id, String name, String email, String createdAt) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.createdAt = createdAt;
        }
        
        // Getters và setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    }

    // Test entity không có primary key
    static class TestEntityNoPrimaryKey {
        @Column("name")
        private String name;
        
        @Column("value")
        private String value;
        
        public TestEntityNoPrimaryKey() {}
        
        public TestEntityNoPrimaryKey(String name, String value) {
            this.name = name;
            this.value = value;
        }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
    }

    // Concrete implementation của BaseDao để test
    static class TestBaseDao extends BaseDao<TestEntity> {
        public TestBaseDao(NamedParameterJdbcTemplate jdbcTemplate, String tableName) {
            super(jdbcTemplate, tableName);
        }
        
        // Expose protected methods for testing
        public Integer publicInsert(TestEntity entity) {
            return super.insert(entity);
        }
        
        public Integer publicUpdate(TestEntity entity, String whereClause) {
            return super.update(entity, whereClause);
        }
        
        public Integer publicUpdate(TestEntity entity) {
            return super.update(entity);
        }
    }

    // Concrete implementation cho entity không có primary key
    static class TestBaseDaoNoPK extends BaseDao<TestEntityNoPrimaryKey> {
        public TestBaseDaoNoPK(NamedParameterJdbcTemplate jdbcTemplate, String tableName) {
            super(jdbcTemplate, tableName);
        }
        
        public Integer publicUpdate(TestEntityNoPrimaryKey entity) {
            return super.update(entity);
        }
    }

    @BeforeEach
    void setUp() {
        testBaseDao = new TestBaseDao(jdbcTemplate, "users");
    }

    // ========== Tests cho insert() method ==========

    @Test
    void testInsert_Success() {
        // Arrange
        TestEntity entity = new TestEntity(1L, "John Doe", "john@example.com", "2023-01-01");
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Act
        Integer result = testBaseDao.publicInsert(entity);

        // Assert
        assertEquals(1, result, "Insert should return 1 for successful insert");
        verify(jdbcTemplate, times(1)).update(anyString(), anyMap()); // Kiểm tra xem phương thức update đã được gọi đúng số lần
    }

    @Test
    void testInsert_WithNullValues() {
        // Arrange
        TestEntity entity = new TestEntity(1L, null, "john@example.com", null);
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Act
        Integer result = testBaseDao.publicInsert(entity);

        // Assert
        assertEquals(1, result, "Insert should handle null values correctly");
        verify(jdbcTemplate, times(1)).update(anyString(), anyMap());
    }

    @Test
    void testInsert_DatabaseError() {
        // Arrange
        TestEntity entity = new TestEntity(1L, "John Doe", "john@example.com", "2023-01-01");
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(0);

        // Act
        Integer result = testBaseDao.publicInsert(entity);

        // Assert
        assertEquals(0, result, "Insert should return 0 when no rows affected");
        verify(jdbcTemplate, times(1)).update(anyString(), anyMap());
    }

    @Test
    void testInsert_VerifySQL() {
        // Arrange
        TestEntity entity = new TestEntity(1L, "John Doe", "john@example.com", "2023-01-01");
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Act
        testBaseDao.publicInsert(entity);

        // Assert - verify SQL query structure
        verify(jdbcTemplate).update(
            argThat(sql -> sql.startsWith("INSERT INTO users") && sql.contains("VALUES")),
            anyMap()
        );
    }

    // ========== Tests cho update(entity, whereClause) method ==========

    @Test
    void testUpdateWithWhereClause_Success() {
        // Arrange
        TestEntity entity = new TestEntity(1L, "John Updated", "john.updated@example.com", "2023-01-01");
        String whereClause = "user_id = :user_id";
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Act
        Integer result = testBaseDao.publicUpdate(entity, whereClause);

        // Assert
        assertEquals(1, result, "Update should return 1 for successful update");
        verify(jdbcTemplate, times(1)).update(anyString(), anyMap());
    }

    @Test
    void testUpdateWithWhereClause_NoRowsAffected() {
        // Arrange
        TestEntity entity = new TestEntity(1L, "John Updated", "john.updated@example.com", "2023-01-01");
        String whereClause = "user_id = :user_id";
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(0);

        // Act
        Integer result = testBaseDao.publicUpdate(entity, whereClause);

        // Assert
        assertEquals(0, result, "Update should return 0 when no rows affected");
        verify(jdbcTemplate, times(1)).update(anyString(), anyMap());
    }

    @Test
    void testUpdateWithWhereClause_ComplexWhereClause() {
        // Arrange
        TestEntity entity = new TestEntity(1L, "John Updated", "john.updated@example.com", "2023-01-01");
        String whereClause = "user_id = :user_id AND status = 'active'";
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Act
        Integer result = testBaseDao.publicUpdate(entity, whereClause);

        // Assert
        assertEquals(1, result, "Update should handle complex WHERE clause");
        verify(jdbcTemplate).update(
            argThat(sql -> sql.contains("WHERE user_id = :user_id AND status = 'active'")),
            anyMap()
        );
    }

    @Test
    void testUpdateWithWhereClause_VerifySQL() {
        // Arrange
        TestEntity entity = new TestEntity(1L, "John Updated", "john.updated@example.com", "2023-01-01");
        String whereClause = "user_id = :user_id";
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Act
        testBaseDao.publicUpdate(entity, whereClause);

        // Assert - verify SQL query structure
        verify(jdbcTemplate).update(
            argThat(sql -> sql.startsWith("UPDATE users SET") && sql.contains("WHERE user_id = :user_id")),
            anyMap()
        );
    }

    // ========== Tests cho update(entity) method (primary key based) ==========

    @Test
    void testUpdateByPrimaryKey_Success() {
        // Arrange
        TestEntity entity = new TestEntity(1L, "John Updated", "john.updated@example.com", "2023-01-01");
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Act
        Integer result = testBaseDao.publicUpdate(entity);

        // Assert
        assertEquals(1, result, "Update by primary key should return 1 for successful update");
        verify(jdbcTemplate, times(1)).update(anyString(), anyMap());
    }

    @Test
    void testUpdateByPrimaryKey_NoPrimaryKeyDefined() {
        // Arrange
        TestEntityNoPrimaryKey entity = new TestEntityNoPrimaryKey("test", "value");
        TestBaseDaoNoPK daoNoPK = new TestBaseDaoNoPK(jdbcTemplate, "test_table");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> daoNoPK.publicUpdate(entity),
            "Should throw IllegalArgumentException when entity has no primary key"
        );
        
        assertEquals("Entity does not have a primary key defined.", exception.getMessage());
        verify(jdbcTemplate, never()).update(anyString(), anyMap());
    }

    @Test
    void testUpdateByPrimaryKey_NullPrimaryKeyValue() {
        // Arrange
        TestEntity entity = new TestEntity(null, "John Updated", "john.updated@example.com", "2023-01-01");
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(0);

        // Act
        Integer result = testBaseDao.publicUpdate(entity);

        // Assert
        assertEquals(0, result, "Update with null primary key should return 0");
        verify(jdbcTemplate, times(1)).update(anyString(), anyMap());
    }

    @Test
    void testUpdateByPrimaryKey_VerifyWhereClauseGeneration() {
        // Arrange
        TestEntity entity = new TestEntity(1L, "John Updated", "john.updated@example.com", "2023-01-01");
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Act
        testBaseDao.publicUpdate(entity);

        // Assert - verify WHERE clause is correctly generated
        verify(jdbcTemplate).update(
            argThat(sql -> {
                // Kiểm tra format của WHERE clause
                return sql.contains("WHERE user_id = :user_id") &&
                       sql.startsWith("UPDATE users SET") &&
                       sql.contains("user_name = :user_name") &&
                       sql.contains("user_email = :user_email");
            }),
            anyMap()
        );
    }

    // ========== Integration Tests ==========

    @Test
    void testTableNameUsage() {
        // Arrange
        TestEntity entity = new TestEntity(1L, "John Doe", "john@example.com", "2023-01-01");
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Act
        testBaseDao.publicInsert(entity);
        testBaseDao.publicUpdate(entity);

        // Assert - verify table name is used correctly in both insert and update
        verify(jdbcTemplate, times(2)).update(
            argThat(sql -> sql.contains("users")),
            anyMap()
        );
    }

    @Test
    void testDifferentTableName() {
        // Arrange
        TestBaseDao customDao = new TestBaseDao(jdbcTemplate, "custom_table");
        TestEntity entity = new TestEntity(1L, "John Doe", "john@example.com", "2023-01-01");
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Act
        customDao.publicInsert(entity);

        // Assert
        verify(jdbcTemplate).update(
            argThat(sql -> sql.contains("custom_table")),
            anyMap()
        );
    }

    @Test
    void testConstructorInitialization() {
        // Arrange & Act
        TestBaseDao dao = new TestBaseDao(jdbcTemplate, "test_table");

        // Assert
        assertNotNull(dao, "DAO should be properly initialized");
        // Verify that the DAO can be used (table name and jdbc template are set)
        TestEntity entity = new TestEntity(1L, "Test", "test@example.com", "2023-01-01");
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);
        
        Integer result = dao.publicInsert(entity);
        assertEquals(1, result, "DAO should work correctly after initialization");
    }

    // ========== Edge Cases ==========

    @Test
    void testInsert_EmptyEntity() {
        // Arrange
        TestEntity entity = new TestEntity(null, null, null, null);
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(0);

        // Act
        Integer result = testBaseDao.publicInsert(entity);

        // Assert
        assertEquals(0, result, "Insert with all null values should return 0");
        verify(jdbcTemplate, times(1)).update(anyString(), anyMap());
    }

    @Test
    void testUpdate_EntityWithNonInsertableFields() {
        // Arrange
        TestEntity entity = new TestEntity(1L, "John", "john@example.com", "2023-01-01");
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Act
        testBaseDao.publicUpdate(entity);

        // Assert - verify that non-insertable field 'created_at' is not included in UPDATE
        verify(jdbcTemplate).update(
            argThat(sql -> !sql.contains("created_at")),
            anyMap()
        );
    }

    @Test
    void testMultipleOperations() {
        // Arrange
        TestEntity entity1 = new TestEntity(1L, "John", "john@example.com", "2023-01-01");
        TestEntity entity2 = new TestEntity(2L, "Jane", "jane@example.com", "2023-01-02");
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Act
        testBaseDao.publicInsert(entity1);
        testBaseDao.publicInsert(entity2);
        testBaseDao.publicUpdate(entity1);

        // Assert
        verify(jdbcTemplate, times(3)).update(anyString(), anyMap());
    }

    // ========== Test Error Scenarios ==========

    @Test
    void testInsert_SQLError() {
        // Arrange
        TestEntity entity = new TestEntity(1L, "John Doe", "john@example.com", "2023-01-01");
        when(jdbcTemplate.update(anyString(), anyMap())).thenThrow(new RuntimeException("SQL Error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> testBaseDao.publicInsert(entity));
        verify(jdbcTemplate, times(1)).update(anyString(), anyMap());
    }

    @Test
    void testUpdate_SQLError() {
        // Arrange
        TestEntity entity = new TestEntity(1L, "John Updated", "john.updated@example.com", "2023-01-01");
        when(jdbcTemplate.update(anyString(), anyMap())).thenThrow(new RuntimeException("SQL Error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> testBaseDao.publicUpdate(entity));
        verify(jdbcTemplate, times(1)).update(anyString(), anyMap());
    }

    // ========== Test SQL Query Validation ==========

    @Test
    void testInsertSQL_ContainsCorrectColumns() {
        // Arrange
        TestEntity entity = new TestEntity(1L, "John Doe", "john@example.com", "2023-01-01");
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Act
        testBaseDao.publicInsert(entity);

        // Assert - verify SQL contains correct insertable columns
        verify(jdbcTemplate).update(
            argThat(sql -> 
                sql.contains("user_id") &&
                sql.contains("user_name") &&
                sql.contains("user_email") &&
                !sql.contains("created_at") // Non-insertable field should not be included
            ),
            anyMap()
        );
    }

    @Test
    void testUpdateSQL_ContainsCorrectSetClause() {
        // Arrange
        TestEntity entity = new TestEntity(1L, "John Updated", "john.updated@example.com", "2023-01-01");
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Act
        testBaseDao.publicUpdate(entity);

        // Assert - verify SQL contains correct SET clause
        verify(jdbcTemplate).update(
            argThat(sql -> 
                sql.contains("SET") &&
                sql.contains("user_name = :user_name") &&
                sql.contains("user_email = :user_email") &&
                !sql.contains("created_at = :created_at") // Non-insertable field should not be included
            ),
            anyMap()
        );
    }
}
