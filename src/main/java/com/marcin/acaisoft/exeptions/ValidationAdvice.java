package com.marcin.acaisoft.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ValidationAdvice {

    @ExceptionHandler(WebExchangeBindException.class)
    ResponseEntity handleConstraintViolationException(WebExchangeBindException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("timestamp", new Date().toString());
        response.put("status", String.valueOf(HttpStatus.BAD_REQUEST.value()));
        response.put("errors", ex.getFieldErrors()
                .stream()
                .map(field -> String.format("%s: %s", field.getField() ,field.getDefaultMessage()))
                .collect(Collectors.toList()).toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
