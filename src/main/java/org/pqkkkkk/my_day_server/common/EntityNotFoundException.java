package org.pqkkkkk.my_day_server.common;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String entityType, Object id) {
        super(String.format("%s with ID %s not found", entityType, id));
    }
}
