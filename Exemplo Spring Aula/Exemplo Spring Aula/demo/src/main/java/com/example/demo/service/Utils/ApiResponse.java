package com.example.demo.service.Utils;

public class ApiResponse<T> {
    
    private boolean success;
    private String message;
    private T data;
    private ErrorResponse error;

    public ApiResponse(T data) {
        this.success = true;
        this.data = data;
        this.error = null;
        this.message = null;
    }

    public ApiResponse(ErrorResponse error) {
        this.success = false;
        this.data = null;
        this.error = error;
        this.message = null;
    }

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.error = null;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ErrorResponse getError() {
        return error;
    }

    public void setError(ErrorResponse error) {
        this.error = error;
    }
}