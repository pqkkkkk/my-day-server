package org.pqkkkkk.my_day_server;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.pqkkkkk.my_day_server.common.SqlReflectionUtils;
import org.pqkkkkk.my_day_server.task.entity.Column;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SqlReflectionUtilsTest {
    
    // Test entity class với các @Column annotations khác nhau
    static class TestEntity {
        @Column(value = "user_id", isPrimaryKey = true)
        private Long id;
        
        @Column("user_name")
        private String name;
        
        @Column(value = "user_email", insertable = true)
        private String email;
        
        @Column(value = "created_at", insertable = false)
        private String createdAt;
        
        // Field không có annotation
        private String description;
        
        // Field với annotation rỗng
        @Column("")
        private Integer age;
        
        // Constructors
        public TestEntity() {}
        
        public TestEntity(Long id, String name, String email, String createdAt, String description, Integer age) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.createdAt = createdAt;
            this.description = description;
            this.age = age;
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
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }
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
    
    private TestEntity testEntity;
    
    @BeforeEach
    void setUp() {
        testEntity = new TestEntity(1L, "John Doe", "john@example.com", "2023-01-01", "Description", 25);
    }
    
    // ========== Tests cho getPrimaryKeyName() ==========
    
    @Test
    void testGetPrimaryKeyName_WithPrimaryKey() {
        // Test với entity có primary key
        String primaryKey = SqlReflectionUtils.getPrimaryKeyName(testEntity);
        assertEquals("user_id", primaryKey, "Primary key name should be 'user_id'");
    }
    
    @Test
    void testGetPrimaryKeyName_NoPrimaryKey() {
        // Test với entity không có primary key
        TestEntityNoPrimaryKey entityNoPK = new TestEntityNoPrimaryKey("test", "value");
        String primaryKey = SqlReflectionUtils.getPrimaryKeyName(entityNoPK);
        assertNull(primaryKey, "Primary key should be null when no primary key exists");
    }
    
    @Test
    void testGetPrimaryKeyName_EmptyEntity() {
        // Test với entity rỗng
        Object emptyEntity = new Object();
        String primaryKey = SqlReflectionUtils.getPrimaryKeyName(emptyEntity);
        assertNull(primaryKey, "Primary key should be null for empty entity");
    }
    
    // ========== Tests cho entityToMap() ==========
    
    @Test
    void testEntityToMap_AllInsertableFields() {
        Map<String, Object> result = SqlReflectionUtils.entityToMap(testEntity);
        
        // Kiểm tra các field insertable được include
        assertTrue(result.containsKey("user_id"), "Should contain primary key");
        assertTrue(result.containsKey("user_name"), "Should contain name field");
        assertTrue(result.containsKey("user_email"), "Should contain email field");
        assertTrue(result.containsKey("age"), "Should contain age field with empty annotation");
        
        // Kiểm tra field không insertable bị exclude
        assertFalse(result.containsKey("created_at"), "Should not contain non-insertable field");

        // Kiểm tra field không có annotation vẫn được include
        assertTrue(result.containsKey("description"), "Should  contain field without annotation");
        
        // Kiểm tra giá trị
        assertEquals(1L, result.get("user_id"));
        assertEquals("John Doe", result.get("user_name"));
        assertEquals("john@example.com", result.get("user_email"));
        assertEquals(25, result.get("age"));
    }
    
    @Test
    void testEntityToMap_WithNullValues() {
        // Test với các field có giá trị null
        TestEntity entityWithNulls = new TestEntity(1L, null, "john@example.com", null, null, null);
        Map<String, Object> result = SqlReflectionUtils.entityToMap(entityWithNulls);
        
        // Chỉ các field không null được include
        assertTrue(result.containsKey("user_id"));
        assertTrue(result.containsKey("user_email"));
        assertFalse(result.containsKey("user_name"), "Null fields should not be included");
        assertFalse(result.containsKey("age"), "Null fields should not be included");
        
        assertEquals(2, result.size(), "Should only contain non-null insertable fields");
    }
    
    @Test
    void testEntityToMap_EmptyEntity() {
        Object emptyEntity = new Object();
        Map<String, Object> result = SqlReflectionUtils.entityToMap(emptyEntity);
        
        assertTrue(result.isEmpty(), "Empty entity should return empty map");
    }
    
    @Test
    void testEntityToMap_AllNullFields() {
        TestEntity allNullEntity = new TestEntity(null, null, null, null, null, null);
        Map<String, Object> result = SqlReflectionUtils.entityToMap(allNullEntity);
        
        assertTrue(result.isEmpty(), "Entity with all null fields should return empty map");
    }
    
    // ========== Tests cho generateInsertQuery() ==========
    
    @Test
    void testGenerateInsertQuery_StandardCase() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("user_id", 1L);
        fields.put("user_name", "John");
        fields.put("user_email", "john@example.com");
        
        String query = SqlReflectionUtils.generateInsertQuery("users", fields);
        
        assertTrue(query.startsWith("INSERT INTO users"), "Query should start with INSERT INTO users");
        assertTrue(query.contains("user_id, user_name, user_email") || 
                  query.contains("user_id, user_email, user_name") ||
                  query.contains("user_name, user_id, user_email") ||
                  query.contains("user_name, user_email, user_id") ||
                  query.contains("user_email, user_id, user_name") ||
                  query.contains("user_email, user_name, user_id"), 
                  "Query should contain all column names");
        assertTrue(query.contains("VALUES (:user_id, :user_name, :user_email)") ||
                  query.contains("VALUES (:user_id, :user_email, :user_name)") ||
                  query.contains("VALUES (:user_name, :user_id, :user_email)") ||
                  query.contains("VALUES (:user_name, :user_email, :user_id)") ||
                  query.contains("VALUES (:user_email, :user_id, :user_name)") ||
                  query.contains("VALUES (:user_email, :user_name, :user_id)"), 
                  "Query should contain all placeholders");
    }
    
    @Test
    void testGenerateInsertQuery_SingleField() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "Test");
        
        String query = SqlReflectionUtils.generateInsertQuery("test_table", fields);
        String expected = "INSERT INTO test_table (name) VALUES (:name)";
        
        assertEquals(expected, query, "Single field insert query should be correct");
    }
    
    @Test
    void testGenerateInsertQuery_EmptyFields() {
        Map<String, Object> fields = new HashMap<>();
        
        String query = SqlReflectionUtils.generateInsertQuery("test_table", fields);
        String expected = "INSERT INTO test_table () VALUES ()";
        
        assertEquals(expected, query, "Empty fields should generate empty insert query");
    }
    
    // ========== Tests cho generateUpdateQuery() ==========
    
    @Test
    void testGenerateUpdateQuery_StandardCase() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("user_name", "John Updated");
        fields.put("user_email", "john.updated@example.com");
        
        String query = SqlReflectionUtils.generateUpdateQuery("users", fields, "user_id = :id");
        
        assertTrue(query.startsWith("UPDATE users SET"), "Query should start with UPDATE users SET");
        assertTrue(query.endsWith("WHERE user_id = :id"), "Query should end with WHERE clause");
        assertTrue(query.contains("user_name = :user_name"), "Query should contain name update");
        assertTrue(query.contains("user_email = :user_email"), "Query should contain email update");
        assertTrue(query.contains(","), "Query should contain comma separator");
    }
    
    @Test
    void testGenerateUpdateQuery_SingleField() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("status", "active");
        
        String query = SqlReflectionUtils.generateUpdateQuery("users", fields, "id = 1");
        String expected = "UPDATE users SET status = :status WHERE id = 1";
        
        assertEquals(expected, query, "Single field update query should be correct");
    }
    
    @Test
    void testGenerateUpdateQuery_EmptyFields() {
        Map<String, Object> fields = new HashMap<>();
        
        String query = SqlReflectionUtils.generateUpdateQuery("users", fields, "id = 1");
        String expected = "UPDATE users SET  WHERE id = 1";
        
        assertEquals(expected, query, "Empty fields should generate empty SET clause");
    }
    
    @Test
    void testGenerateUpdateQuery_ComplexWhereClause() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("status", "inactive");
        
        String whereClause = "user_id = :userId AND created_at < :date";
        String query = SqlReflectionUtils.generateUpdateQuery("users", fields, whereClause);
        String expected = "UPDATE users SET status = :status WHERE user_id = :userId AND created_at < :date";
        
        assertEquals(expected, query, "Complex WHERE clause should be preserved");
    }
    
    // ========== Integration Tests ==========
    
    @Test
    void testIntegration_EntityToMapToInsertQuery() {
        // Test tích hợp: từ entity -> map -> insert query
        Map<String, Object> entityMap = SqlReflectionUtils.entityToMap(testEntity);
        String insertQuery = SqlReflectionUtils.generateInsertQuery("users", entityMap);
        
        assertNotNull(insertQuery, "Insert query should not be null");
        assertTrue(insertQuery.contains("INSERT INTO users"), "Should be valid insert query");
        assertTrue(insertQuery.contains("user_id"), "Should contain primary key");
        assertTrue(insertQuery.contains(":user_id"), "Should contain primary key placeholder");
    }
    
    @Test
    void testIntegration_EntityToMapToUpdateQuery() {
        // Test tích hợp: từ entity -> map -> update query
        Map<String, Object> entityMap = SqlReflectionUtils.entityToMap(testEntity);
        String primaryKey = SqlReflectionUtils.getPrimaryKeyName(testEntity);
        String whereClause = primaryKey + " = :" + primaryKey;
        String updateQuery = SqlReflectionUtils.generateUpdateQuery("users", entityMap, whereClause);
        
        assertNotNull(updateQuery, "Update query should not be null");
        assertTrue(updateQuery.contains("UPDATE users SET"), "Should be valid update query");
        assertTrue(updateQuery.contains("WHERE user_id = :user_id"), "Should contain correct WHERE clause");
    }
}
