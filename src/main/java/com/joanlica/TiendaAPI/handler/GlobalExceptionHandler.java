package com.joanlica.TiendaAPI.handler;

import com.joanlica.TiendaAPI.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException e) {
        Map<String,Object> map = new HashMap<>();
        map.put("message",e.getMessage());
        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND); //404
    }

    // Handler para las Validaciones
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errores = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        Map<String, List<String>> body = new HashMap<>();
        body.put("errores", errores);

        // Return a ResponseEntity to explicitly specify the response body and status
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}