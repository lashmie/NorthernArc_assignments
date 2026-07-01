package org.northernarc.loanminiproject.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
    private String error;
    private String path;
    private LocalDateTime timestamp;
    private Map<String, String> validationErrors;

    public ErrorResponse(int status, String message, String error, String path) {
        this.status = status;
        this.message = message;
        this.error = error;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }
}

