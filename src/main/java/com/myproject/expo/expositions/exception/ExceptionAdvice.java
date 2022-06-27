package com.myproject.expo.expositions.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

//@ControllerAdvice
@Slf4j
public class ExceptionAdvice {

    //@ExceptionHandler(Exception.class)
    public ResponseEntity<ApiExceptionHandler> handleExceptions(Exception e, HttpServletRequest request) {
        log.info("ExceptionAdvice works  = " + e.getMessage());
        ApiExceptionHandler err = new ApiExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                e.getMessage(), request.getServletPath());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);

    }
}
