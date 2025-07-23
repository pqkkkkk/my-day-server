package org.pqkkkkk.my_day_server.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public abstract class BaseDao<T> {
    protected final NamedParameterJdbcTemplate jdbcTemplate;
    protected final String tableName;

    public BaseDao(NamedParameterJdbcTemplate jdbcTemplate, String tableName) {
        this.jdbcTemplate = jdbcTemplate;
        this.tableName = tableName;
    }

    protected Integer insert(T entity){
        Map<String, Object> paramMap = SqlReflectionUtils.entityToMap(entity);
        String sql = SqlReflectionUtils.generateInsertQuery(tableName, paramMap);

        return jdbcTemplate.update(sql, paramMap);
    }
    /**
     * This method is used to update an entity in the database.
     * It takes an entity and a where clause as parameters.
     * The where clause is used to specify which record(s) to update.
     */
    protected Integer update(T entity, String whereClause, Map<String, Object> whereParams) {
        Map<String, Object> setParams = SqlReflectionUtils.entityToMap(entity);
        Map<String, Object> allParams = new HashMap<>(setParams);
        
        if (whereParams != null) {
            allParams.putAll(whereParams);
        }
        
        String sql = SqlReflectionUtils.generateUpdateQuery(tableName, setParams, whereClause);
        return jdbcTemplate.update(sql, allParams);
    }
    /**
     * This method is a convenience method for updating an entity by its primary key.
     */
    protected Integer update(T entity){
        String primaryKeyName = SqlReflectionUtils.getPrimaryKeyName(entity);
        Object primaryKeyValue = SqlReflectionUtils.getPrimaryKeyValue(entity);

        if (primaryKeyName == null) {
            throw new IllegalArgumentException("Entity does not have a primary key defined.");
        }
        if (primaryKeyValue == null) {
            throw new IllegalArgumentException("Primary key value cannot be null.");
        }
        String whereClause = primaryKeyName + " = :" + primaryKeyName;
        Map<String, Object> setParams = SqlReflectionUtils.entityToMap(entity);
        setParams.remove(primaryKeyName);

        // Add the primary key to the parameter map for the where clause
        Map<String, Object> allParams = new HashMap<>(setParams);
        allParams.put(primaryKeyName, primaryKeyValue);

        String sql = SqlReflectionUtils.generateUpdateQuery(tableName, setParams, whereClause);
        return jdbcTemplate.update(sql, allParams);
    }
    protected Integer delete(T entity){
        String primaryKeyName = SqlReflectionUtils.getPrimaryKeyName(entity);
        Object primaryKeyValue = SqlReflectionUtils.getPrimaryKeyValue(entity);

        if (primaryKeyName == null) {
            throw new IllegalArgumentException("Entity does not have a primary key defined.");
        }
        if (primaryKeyValue == null) {
            throw new IllegalArgumentException("Primary key value cannot be null.");
        }

        String sql = "DELETE FROM " + tableName + " WHERE " + primaryKeyName + " = :" + primaryKeyName;
        Map<String, Object> params = new HashMap<>();
        params.put(primaryKeyName, primaryKeyValue);

        return jdbcTemplate.update(sql, params);
    }
    protected T queryForObject(String sql, Map<String, Object> params, Class<T> clazz) {
        try{
        return jdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(clazz));
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
