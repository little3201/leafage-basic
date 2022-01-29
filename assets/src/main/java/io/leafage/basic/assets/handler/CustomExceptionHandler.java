/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Exception handler.
 *
 * @author liwenqiang  2020-12-20 9:54
 */
@RestControllerAdvice
public class CustomExceptionHandler {

    /**
     * 处理参数校验异常
     *
     * @param exception exception of MethodArgumentNotValidException
     * @return a construct of ResponseEntity
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : fieldErrors) {
            builder.append(fieldError.getField()).append(fieldError.getDefaultMessage()).append(",");
        }
        return ResponseEntity.of(Optional.of(builder.toString()));
    }

    /**
     * 处理递归参数校验异常
     *
     * @param exception exception of ConstraintViolationException
     * @return a construct of ResponseEntity
     */
    @ExceptionHandler
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        StringBuilder builder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            builder.append(violation.getMessage()).append(",");
        }
        return ResponseEntity.of(Optional.of(builder.toString()));
    }
}
