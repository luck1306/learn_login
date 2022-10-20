package com.example.learn_login.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ProjectException.class})
    protected ResponseEntity<ErrorResponse> HandlerProjectException(ProjectException e) {
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse(e.getErrorCode().getStatus(), e.getErrorCode().getMessage()),
                HttpStatus.valueOf(e.getErrorCode().getStatus()));
    }
}
