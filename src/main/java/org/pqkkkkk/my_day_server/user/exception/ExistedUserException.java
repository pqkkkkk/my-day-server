package org.pqkkkkk.my_day_server.user.exception;

public class ExistedUserException extends RuntimeException {
    public ExistedUserException(String message) {
        super(message);
    }
}
