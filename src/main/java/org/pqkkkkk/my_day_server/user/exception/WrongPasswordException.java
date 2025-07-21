package org.pqkkkkk.my_day_server.user.exception;

public class WrongPasswordException extends RuntimeException {

    public WrongPasswordException() {
        super("The provided password is incorrect.");
    }

    public WrongPasswordException(String message) {
        super(message);
    }

    public WrongPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongPasswordException(Throwable cause) {
        super(cause);
    }

}
