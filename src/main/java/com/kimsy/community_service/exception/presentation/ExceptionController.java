package com.kimsy.community_service.exception.presentation;

import com.kimsy.community_service.exception.presentation.dto.ExceptionResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ExceptionResult communityServiceExceptionHandler(IllegalArgumentException e) {
        return new ExceptionResult("404 BAD_REQUEST", e.getMessage());
    }
}
