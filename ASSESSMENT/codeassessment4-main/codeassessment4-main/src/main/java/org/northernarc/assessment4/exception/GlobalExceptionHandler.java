package org.northernarc.assessment4.exception;

import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .distinct()
                .toList();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.name());
        body.put("message", "Validation failed");
        body.put("errors", errors);

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleCustomerNotFound(CustomerNotFoundException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", HttpStatus.NOT_FOUND.name());
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleAccountNotFound(AccountNotFoundException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", HttpStatus.NOT_FOUND.name());
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleValidationException(ValidationException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.name());
        body.put("message", ex.getMessage());
        body.put("errors", List.of(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    private String formatFieldError(FieldError error) {
        return error.getField() + ": " + error.getDefaultMessage();
    }
}

