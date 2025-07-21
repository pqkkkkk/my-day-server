package org.pqkkkkk.my_day_server.common;

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
    protected Integer update(T entity, String whereClause){
        Map<String, Object> paramMap = SqlReflectionUtils.entityToMap(entity);
        String sql = SqlReflectionUtils.generateUpdateQuery(tableName, paramMap, whereClause);

        return jdbcTemplate.update(sql, paramMap);
    }
    /**
     * This method is a convenience method for updating an entity by its primary key.
     */
    protected Integer update(T entity){
        String primaryKeyName = SqlReflectionUtils.getPrimaryKeyName(entity);

        if (primaryKeyName == null) {
            throw new IllegalArgumentException("Entity does not have a primary key defined.");
        }
        
        String whereClause = primaryKeyName + " = :" + primaryKeyName;

        return update(entity, whereClause);
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
