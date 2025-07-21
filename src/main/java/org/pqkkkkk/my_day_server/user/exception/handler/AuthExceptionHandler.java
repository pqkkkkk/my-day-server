package org.pqkkkkk.my_day_server.user.exception.handler;

import org.pqkkkkk.my_day_server.common.ApiError;
import org.pqkkkkk.my_day_server.user.exception.ExistedUserException;
import org.pqkkkkk.my_day_server.user.exception.UserNotFoundException;
import org.pqkkkkk.my_day_server.user.exception.WrongPasswordException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "org.pqkkkkk.my_day_server.user")
@Order(1)
public class AuthExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException ex) {
        ApiError apiError = new ApiError("User not found", HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<ApiError> handleWrongPasswordException(WrongPasswordException ex) {
        ApiError apiError = new ApiError("Wrong password", HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }

    @ExceptionHandler(ExistedUserException.class)
    public ResponseEntity<ApiError> handleExistedUserException(ExistedUserException ex) {
        ApiError apiError = new ApiError("User already exists", HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }
}
