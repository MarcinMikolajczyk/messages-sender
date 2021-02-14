package com.marcin.acaisoft.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class MessageNotFoundExeptionAdvice {
    @ExceptionHandler(MessageNotFoundExeption.class)
    ResponseEntity handleMessageNotFoundExeption(MessageNotFoundExeption ex) {
        Map<String, String> response = new HashMap<>();
        response.put("timestamp", ex.getExceptionTime().toString());
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
