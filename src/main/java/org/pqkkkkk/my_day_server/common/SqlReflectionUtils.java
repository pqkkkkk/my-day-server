package org.pqkkkkk.my_day_server.common;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.pqkkkkk.my_day_server.task.entity.Column;


/* Utility methods for SQL reflection 
 * This class is intended to provide methods for dynamically accessing and manipulating SQL-related annotations and properties of entities.
 */
public class SqlReflectionUtils {
    public static String getPrimaryKeyName(Object entity) {
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            Column columnAnnotation = field.getAnnotation(Column.class);
            if (columnAnnotation != null && columnAnnotation.isPrimaryKey()) {
                return columnAnnotation.value().isEmpty() ? field.getName() : columnAnnotation.value();
            }
        }
        return null; // No primary key found
    }
    public static Map<String, Object> entityToMap(Object entity){
        Map<String, Object> res = new HashMap<>();
        Field[] fields = entity.getClass().getDeclaredFields();

        for(Field field : fields){
            field.setAccessible(true);
            Column columnAnnotation =field.getAnnotation(Column.class);

            if(columnAnnotation != null && !columnAnnotation.insertable()) {
                continue; // Skip fields that are not insertable
            }

            try{
                Object value = field.get(entity);
                if(value != null) {
                    String columnName = columnAnnotation != null && !columnAnnotation.value().isEmpty() 
                            ? columnAnnotation.value() 
                            : field.getName();
                    res.put(columnName, value);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access field: " + field.getName(), e);
            } finally {
                field.setAccessible(false);
            }

        }

        return res;
    }
    public static String generateInsertQuery(String tableName, Map<String, Object> fields){
        String columns = String.join(", ", fields.keySet());
        String placeholders = fields.keySet().stream()
                .map(key -> ":" + key)
                .collect(Collectors.joining(", "));

        return String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, columns, placeholders);
    }

    public static String generateUpdateQuery(String tableName, Map<String, Object> fields, String whereClause){
        String setClause = fields.keySet().stream()
                .map(key -> key + " = :" + key)
                .collect(Collectors.joining(", "));

        return String.format("UPDATE %s SET %s WHERE %s", tableName, setClause, whereClause);
    }
}
