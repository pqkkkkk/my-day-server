package org.pqkkkkk.my_day_server.common;

import lombok.Data;

@Data
public class ApiResponse<T> {
    T data;
    boolean success;
    int statusCode;
    String message;

    public ApiResponse(T data, boolean success, int statusCode, String message) {
        this.data = data;
        this.success = success;
        this.statusCode = statusCode;
        this.message = message;
    }
}
