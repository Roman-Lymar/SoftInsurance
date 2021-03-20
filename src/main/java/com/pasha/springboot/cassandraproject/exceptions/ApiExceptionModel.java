package com.pasha.springboot.cassandraproject.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public class ApiExceptionModel {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private final LocalDateTime timestamp;
    private final HttpStatus httpStatus;
    private final String message;
    private final List errors;

    public ApiExceptionModel(LocalDateTime timestamp,
                             HttpStatus httpStatus,
                             String message,
                             List errors) {
        this.timestamp = timestamp;
        this.httpStatus = httpStatus;
        this.message = message;
        this.errors = errors;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public List getErrors() {
        return errors;
    }
}
