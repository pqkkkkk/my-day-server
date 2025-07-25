package org.pqkkkkk.my_day_server.common;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
@Order(1)
public class ApiRequestValidationExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException ex) {
    // Handle validation errors
    ApiError errorResponse = new ApiError(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
    return ResponseEntity.badRequest().body(errorResponse);
}
}
