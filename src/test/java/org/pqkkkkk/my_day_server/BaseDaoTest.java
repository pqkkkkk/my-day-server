package org.pqkkkkk.my_day_server;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pqkkkkk.my_day_server.common.BaseDao;
import org.pqkkkkk.my_day_server.task.entity.Column;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.HashMap;
import java.util.Map;

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
        
        public Integer publicUpdate(TestEntity entity, String whereClause, Map<String, Object> whereParams) {
            return super.update(entity, whereClause, whereParams);
        }
        
        public Integer publicUpdate(TestEntity entity) {
            return super.update(entity);
        }
        
        public Integer publicDelete(TestEntity entity) {
            return super.delete(entity);
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
        
        public Integer publicDelete(TestEntityNoPrimaryKey entity) {
            return super.delete(entity);
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

    // ========== Tests cho update(entity, whereClause, whereParams) method ==========

    @Test
    void testUpdateWithWhereClauseAndParams_Success() {
        // Arrange
        TestEntity entity = new TestEntity(1L, "John Updated", "john.updated@example.com", "2023-01-01");
        String whereClause = "user_id = :userId AND status = :status";
        Map<String, Object> whereParams = new HashMap<>();
        whereParams.put("userId", 1L);
        whereParams.put("status", "active");
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Act
        Integer result = testBaseDao.publicUpdate(entity, whereClause, whereParams);

        // Assert
        assertEquals(1, result, "Update should return 1 for successful update");
        verify(jdbcTemplate, times(1)).update(anyString(), anyMap());
    }

    @Test
    void testUpdateWithWhereClauseAndParams_NoRowsAffected() {
        // Arrange
        TestEntity entity = new TestEntity(1L, "John Updated", "john.updated@example.com", "2023-01-01");
        String whereClause = "user_id = :userId";
        Map<String, Object> whereParams = new HashMap<>();
        whereParams.put("userId", 999L); // Non-existent ID
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(0);

        // Act
        Integer result = testBaseDao.publicUpdate(entity, whereClause, whereParams);

        // Assert
        assertEquals(0, result, "Update should return 0 when no rows affected");
        verify(jdbcTemplate, times(1)).update(anyString(), anyMap());
    }

    @Test
    // Chưa hợp lý
    void testUpdateWithWhereClauseAndParams_NullWhereParams() {
        // Arrange
        TestEntity entity = new TestEntity(1L, "John Updated", "john.updated@example.com", "2023-01-01");
        String whereClause = "user_id = :user_id"; // Using field from entity
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Act
        Integer result = testBaseDao.publicUpdate(entity, whereClause, null);

        // Assert
        assertEquals(1, result, "Update should work with null whereParams");
        verify(jdbcTemplate, times(1)).update(anyString(), anyMap());
    }

    @Test
    void testUpdateWithWhereClauseAndParams_ComplexWhereClause() {
        // Arrange
        TestEntity entity = new TestEntity(1L, "John Updated", "john.updated@example.com", "2023-01-01");
        String whereClause = "user_id = :userId AND created_date BETWEEN :startDate AND :endDate";
        Map<String, Object> whereParams = new HashMap<>();
        whereParams.put("userId", 1L);
        whereParams.put("startDate", "2023-01-01");
        whereParams.put("endDate", "2023-12-31");
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Act
        Integer result = testBaseDao.publicUpdate(entity, whereClause, whereParams);

        // Assert
        assertEquals(1, result, "Update should handle complex WHERE clause with multiple parameters");
        verify(jdbcTemplate).update(
            argThat(sql -> sql.contains("WHERE user_id = :userId AND created_date BETWEEN :startDate AND :endDate")),
            anyMap()
        );
    }

    @Test
    void testUpdateWithWhereClauseAndParams_VerifyParameterMerging() {
        // Arrange
        TestEntity entity = new TestEntity(1L, "John Updated", "john.updated@example.com", "2023-01-01");
        String whereClause = "status = :status";
        Map<String, Object> whereParams = new HashMap<>();
        whereParams.put("status", "active");
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Act
        testBaseDao.publicUpdate(entity, whereClause, whereParams);

        // Assert - verify SQL contains both entity fields and where params
        verify(jdbcTemplate).update(
            argThat(sql -> sql.contains("SET") && sql.contains("WHERE status = :status")),
            anyMap()
        );
    }

    @Test
    void testUpdateWithWhereClauseAndParams_ParameterOverride() {
        // Arrange - test case where whereParams overrides entity field
        TestEntity entity = new TestEntity(1L, "John Updated", "john.updated@example.com", "2023-01-01");
        String whereClause = "user_id = :user_id"; // Same parameter name as entity field
        Map<String, Object> whereParams = new HashMap<>();
        whereParams.put("user_id", 2L); // Different value from entity
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Act
        testBaseDao.publicUpdate(entity, whereClause, whereParams);

        // Assert - verify that update is called with merged parameters
        verify(jdbcTemplate).update(
            argThat(sql -> sql.contains("WHERE user_id = :user_id")),
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

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> testBaseDao.publicUpdate(entity),
            "Should throw IllegalArgumentException when primary key value is null"
        );
        
        assertEquals("Primary key value cannot be null.", exception.getMessage());
        verify(jdbcTemplate, never()).update(anyString(), anyMap());
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
                       sql.contains("user_email = :user_email") &&
                       !sql.contains("user_id = :user_id,"); // Primary key should not be in SET clause
            }),
            anyMap()
        );
    }

    @Test
    void testUpdateByPrimaryKey_PrimaryKeyNotInSetClause() {
        // Arrange
        TestEntity entity = new TestEntity(1L, "John Updated", "john.updated@example.com", "2023-01-01");
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Act
        testBaseDao.publicUpdate(entity);

        // Assert - verify primary key is NOT in SET clause but IS in WHERE clause
        verify(jdbcTemplate).update(
            argThat(sql -> {
                String[] parts = sql.split("WHERE");
                String setClause = parts[0];
                String whereClause = parts.length > 1 ? parts[1] : "";
                
                return !setClause.contains("user_id =") &&        // Primary key should NOT be in SET
                       whereClause.contains("user_id = :user_id"); // Primary key should be in WHERE
            }),
            anyMap()
        );
    }

    @Test
    void testUpdateByPrimaryKey_VerifyParameterSeparation() {
        // Arrange
        TestEntity entity = new TestEntity(1L, "John Updated", "john.updated@example.com", "2023-01-01");
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Act
        testBaseDao.publicUpdate(entity);

        // Assert - verify SQL structure is correct
        verify(jdbcTemplate).update(
            argThat(sql -> {
                // Should have SET clause with non-primary fields
                boolean hasCorrectSetClause = sql.contains("SET") &&
                                            sql.contains("user_name = :user_name") &&
                                            sql.contains("user_email = :user_email") &&
                                            !sql.contains("SET user_id =") &&        // Primary key should NOT be in SET
                                            !sql.contains("created_at = :created_at"); // Non-insertable should be excluded
                
                // Should have WHERE clause with primary key
                boolean hasCorrectWhereClause = sql.contains("WHERE user_id = :user_id");
                
                return hasCorrectSetClause && hasCorrectWhereClause;
            }),
            anyMap()
        );
    }

    // ========== Tests cho delete(entity) method ==========

    @Test
    void testDelete_Success() {
        // Arrange
        TestEntity entity = new TestEntity(1L, "John Doe", "john@example.com", "2023-01-01");
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Act
        Integer result = testBaseDao.publicDelete(entity);

        // Assert
        assertEquals(1, result, "Delete should return 1 for successful deletion");
        verify(jdbcTemplate, times(1)).update(anyString(), anyMap());
    }

    @Test
    void testDelete_NoRowsAffected() {
        // Arrange
        TestEntity entity = new TestEntity(999L, "Non-existent", "non@example.com", "2023-01-01");
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(0);

        // Act
        Integer result = testBaseDao.publicDelete(entity);

        // Assert
        assertEquals(0, result, "Delete should return 0 when no rows affected");
        verify(jdbcTemplate, times(1)).update(anyString(), anyMap());
    }

    @Test
    void testDelete_NoPrimaryKeyDefined() {
        // Arrange
        TestEntityNoPrimaryKey entity = new TestEntityNoPrimaryKey("test", "value");
        TestBaseDaoNoPK daoNoPK = new TestBaseDaoNoPK(jdbcTemplate, "test_table");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> daoNoPK.publicDelete(entity),
            "Should throw IllegalArgumentException when entity has no primary key"
        );
        
        assertEquals("Entity does not have a primary key defined.", exception.getMessage());
        verify(jdbcTemplate, never()).update(anyString(), anyMap());
    }

    @Test
    void testDelete_NullPrimaryKeyValue() {
        // Arrange
        TestEntity entity = new TestEntity(null, "John Doe", "john@example.com", "2023-01-01");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> testBaseDao.publicDelete(entity),
            "Should throw IllegalArgumentException when primary key value is null"
        );
        
        assertEquals("Primary key value cannot be null.", exception.getMessage());
        verify(jdbcTemplate, never()).update(anyString(), anyMap());
    }

    @Test
    void testDelete_VerifySQL() {
        // Arrange
        TestEntity entity = new TestEntity(1L, "John Doe", "john@example.com", "2023-01-01");
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Act
        testBaseDao.publicDelete(entity);

        // Assert - verify DELETE SQL query structure
        verify(jdbcTemplate).update(
            argThat(sql -> {
                return sql.equals("DELETE FROM users WHERE user_id = :user_id");
            }),
            anyMap()
        );
    }

    @Test
    void testDelete_VerifyParameters() {
        // Arrange
        TestEntity entity = new TestEntity(123L, "John Doe", "john@example.com", "2023-01-01");
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Act
        testBaseDao.publicDelete(entity);

        // Assert - verify correct SQL and that parameters are passed
        verify(jdbcTemplate).update(
            eq("DELETE FROM users WHERE user_id = :user_id"),
            anyMap()
        );
    }

    @Test
    void testDelete_DifferentTableName() {
        // Arrange
        TestBaseDao customDao = new TestBaseDao(jdbcTemplate, "products");
        TestEntity entity = new TestEntity(1L, "Product", "product@example.com", "2023-01-01");
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Act
        customDao.publicDelete(entity);

        // Assert - verify correct table name in DELETE query
        verify(jdbcTemplate).update(
            eq("DELETE FROM products WHERE user_id = :user_id"),
            anyMap()
        );
    }

    @Test
    void testDelete_SQLError() {
        // Arrange
        TestEntity entity = new TestEntity(1L, "John Doe", "john@example.com", "2023-01-01");
        when(jdbcTemplate.update(anyString(), anyMap())).thenThrow(new RuntimeException("SQL Error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> testBaseDao.publicDelete(entity));
        verify(jdbcTemplate, times(1)).update(anyString(), anyMap());
    }

    @Test
    void testDelete_MultipleEntitiesWithDifferentIds() {
        // Arrange
        TestEntity entity1 = new TestEntity(1L, "John", "john@example.com", "2023-01-01");
        TestEntity entity2 = new TestEntity(2L, "Jane", "jane@example.com", "2023-01-02");
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Act
        Integer result1 = testBaseDao.publicDelete(entity1);
        Integer result2 = testBaseDao.publicDelete(entity2);

        // Assert
        assertEquals(1, result1, "First delete should succeed");
        assertEquals(1, result2, "Second delete should succeed");
        verify(jdbcTemplate, times(2)).update(anyString(), anyMap());
        
        // Verify correct SQL was called
        verify(jdbcTemplate, times(2)).update(
            eq("DELETE FROM users WHERE user_id = :user_id"),
            anyMap()
        );
    }

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
    void testUpdateByPrimaryKey_SQLError() {
        // Arrange
        TestEntity entity = new TestEntity(1L, "John Updated", "john.updated@example.com", "2023-01-01");
        when(jdbcTemplate.update(anyString(), anyMap())).thenThrow(new RuntimeException("SQL Error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> testBaseDao.publicUpdate(entity));
        verify(jdbcTemplate, times(1)).update(anyString(), anyMap());
    }

    @Test
    void testUpdateWithWhereClauseAndParams_SQLError() {
        // Arrange
        TestEntity entity = new TestEntity(1L, "John Updated", "john.updated@example.com", "2023-01-01");
        String whereClause = "user_id = :userId";
        Map<String, Object> whereParams = new HashMap<>();
        whereParams.put("userId", 1L);
        when(jdbcTemplate.update(anyString(), anyMap())).thenThrow(new RuntimeException("SQL Error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> testBaseDao.publicUpdate(entity, whereClause, whereParams));
        verify(jdbcTemplate, times(1)).update(anyString(), anyMap());
    }

    @Test
    void testUpdateWithWhereClauseAndParams_EmptyWhereParams() {
        // Arrange
        TestEntity entity = new TestEntity(1L, "John Updated", "john.updated@example.com", "2023-01-01");
        String whereClause = "user_id = :user_id"; // Uses parameter from entity
        Map<String, Object> whereParams = new HashMap<>(); // Empty map
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Act
        Integer result = testBaseDao.publicUpdate(entity, whereClause, whereParams);

        // Assert
        assertEquals(1, result, "Update should work with empty whereParams map");
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
                !sql.contains("created_at = :created_at") && // Non-insertable field should not be included
                !sql.contains("user_id = :user_id,")        // Primary key should not be in SET clause
            ),
            anyMap()
        );
    }

    // ========== Integration Tests cho các phương thức update ==========

    @Test
    void testBothUpdateMethods_SameEntity() {
        // Arrange
        TestEntity entity = new TestEntity(1L, "John", "john@example.com", "2023-01-01");
        String whereClause = "status = :status";
        Map<String, Object> whereParams = new HashMap<>();
        whereParams.put("status", "active");
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Act - call both update methods
        Integer result1 = testBaseDao.publicUpdate(entity); // Primary key based
        Integer result2 = testBaseDao.publicUpdate(entity, whereClause, whereParams); // Custom where

        // Assert
        assertEquals(1, result1, "Primary key update should succeed");
        assertEquals(1, result2, "Custom where update should succeed");
        verify(jdbcTemplate, times(2)).update(anyString(), anyMap());
    }

    @Test
    void testUpdateMethodsWithDifferentTableNames() {
        // Arrange
        TestBaseDao dao1 = new TestBaseDao(jdbcTemplate, "users");
        TestBaseDao dao2 = new TestBaseDao(jdbcTemplate, "customers");
        TestEntity entity = new TestEntity(1L, "John", "john@example.com", "2023-01-01");
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Act
        dao1.publicUpdate(entity);
        dao2.publicUpdate(entity);

        // Assert - verify different table names are used
        verify(jdbcTemplate).update(
            argThat(sql -> sql.contains("UPDATE users SET")),
            anyMap()
        );
        verify(jdbcTemplate).update(
            argThat(sql -> sql.contains("UPDATE customers SET")),
            anyMap()
        );
    }

    @Test
    void testCompleteWorkflow_InsertThenUpdate() {
        // Arrange
        TestEntity entity = new TestEntity(1L, "John", "john@example.com", "2023-01-01");
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Act - simulate complete workflow
        testBaseDao.publicInsert(entity);           // Insert
        entity.setName("John Updated");
        testBaseDao.publicUpdate(entity);           // Update by primary key
        
        String whereClause = "status = :status";
        Map<String, Object> whereParams = new HashMap<>();
        whereParams.put("status", "active");
        testBaseDao.publicUpdate(entity, whereClause, whereParams); // Custom update

        // Assert
        verify(jdbcTemplate, times(3)).update(anyString(), anyMap());
        
        // Verify INSERT query
        verify(jdbcTemplate).update(
            argThat(sql -> sql.startsWith("INSERT INTO users")),
            anyMap()
        );
        
        // Verify UPDATE queries
        verify(jdbcTemplate, times(2)).update(
            argThat(sql -> sql.startsWith("UPDATE users SET")),
            anyMap()
        );
    }
}
