package com.myproject.expo.expositions.exception;

import java.time.LocalDateTime;

public class ApiExceptionHandler {
    private int status;
    private String message;
    private String path;
   private LocalDateTime timestamp;

    public ApiExceptionHandler(int status, String message, String path) {
        super();
        this.status = status;
        this.message = message;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getTimeStamp() {
        return timestamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timestamp = timeStamp;
    }
}
