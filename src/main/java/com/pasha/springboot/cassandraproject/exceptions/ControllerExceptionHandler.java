package com.pasha.springboot.cassandraproject.exceptions;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    private final static Logger logger = LogManager.getLogger(ControllerExceptionHandler.class);


   @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException e) {
        logger.error(e.getMessage(), e);

        List<String> details = new ArrayList<>();
        details.add(e.getMessage());

        ApiExceptionModel apiExceptionModel = new ApiExceptionModel(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                "Resource not found!",
                details);
        return ResponceEntityBuilder.build(apiExceptionModel);
    }

    @ExceptionHandler(EmptyPackageException.class)
    protected ResponseEntity<Object> handleEmptyPackageException(EmptyPackageException e) {
        logger.error(e.getMessage(), e);

        List<String> details = new ArrayList<>();
        details.add(e.getMessage());

        ApiExceptionModel apiExceptionModel = new ApiExceptionModel(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                "Empty products list",
                details);
        return ResponceEntityBuilder.build(apiExceptionModel);
    }

    @ExceptionHandler(UnableDeleteProductException.class)
    protected ResponseEntity<Object> handleUnableDeleteProductException(UnableDeleteProductException e) {
        logger.error(e.getMessage(), e);

        List<String> details = new ArrayList<>();
        details.add(e.getMessage());

        ApiExceptionModel apiExceptionModel = new ApiExceptionModel(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                "Product with provided id is used",
                details);
        return ResponceEntityBuilder.build(apiExceptionModel);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        logger.error(e.getMessage(), e);

        List<String> details = new ArrayList<>();
        details = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getObjectName()+ " : " +error.getDefaultMessage())
                .collect(Collectors.toList());

        ApiExceptionModel apiExceptionModel = new ApiExceptionModel(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                "Validation Errors",
                details);
        return ResponceEntityBuilder.build(apiExceptionModel);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        logger.error(e.getMessage(), e);

        List<String> details = new ArrayList<>();
        details.add(e.getMessage());

        ApiExceptionModel apiExceptionModel = new ApiExceptionModel(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                "Type Mismatch",
                details);
        return ResponceEntityBuilder.build(apiExceptionModel);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter( MissingServletRequestParameterException e,
                                                                           HttpHeaders headers,
                                                                           HttpStatus status,
                                                                           WebRequest request) {
        logger.error(e.getMessage(), e);

        List<String> details = new ArrayList<>();
        details.add(e.getMessage());

        ApiExceptionModel apiExceptionModel = new ApiExceptionModel(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                "Missing Parameters",
                details);
        return ResponceEntityBuilder.build(apiExceptionModel);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException e,
                                                                   HttpHeaders headers,
                                                                   HttpStatus status,
                                                                   WebRequest request) {
        logger.error(e.getMessage(), e);

        List<String> details = new ArrayList<>();
        details.add(String.format("Could not find the %s method for URL %s", e.getHttpMethod(), e.getRequestURL()));

        ApiExceptionModel apiExceptionModel = new ApiExceptionModel(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                "Method Not Found",
                details);
        return ResponceEntityBuilder.build(apiExceptionModel);
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> handleAllExceptions(Exception e, WebRequest request) {
        logger.error(e.getLocalizedMessage(), e);

        List<String> details = new ArrayList<>();
        details.add(e.getLocalizedMessage());

        ApiExceptionModel apiExceptionModel = new ApiExceptionModel(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error occurred",
                details);
        return ResponceEntityBuilder.build(apiExceptionModel);
    }
}
