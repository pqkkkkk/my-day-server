package org.pqkkkkk.my_day_server.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String value() default ""; // name of the column in the database
    boolean insertable() default true; // whether the column can be inserted
    boolean updatable() default true; // whether the column can be updated
    boolean isPrimaryKey() default false; // whether the column is a primary key
}
