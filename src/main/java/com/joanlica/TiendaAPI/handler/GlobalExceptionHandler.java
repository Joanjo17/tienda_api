package com.joanlica.TiendaAPI.handler;

import com.joanlica.TiendaAPI.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException e) {
        Map<String,Object> map = new HashMap<>();
        map.put("message",e.getMessage());
        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND); //404
    }
}