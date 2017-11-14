package com.bon.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.ResourceAccessException;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(MethodArgumentNotValidException e, HttpServletResponse response) throws Exception {
        log.warn("Returning HTTP 400 Bad Request", e);
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handle(ResourceNotFoundException e, HttpServletResponse response) throws Exception {
        log.warn("Returning HTTP 404 Not Found", e);
        response.sendError(HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public void handle(ResourceAccessException e, HttpServletResponse response) throws Exception {
        log.warn("Returning HTTP 409 Conflict", e);
        response.sendError(HttpStatus.CONFLICT.value());
    }
}
