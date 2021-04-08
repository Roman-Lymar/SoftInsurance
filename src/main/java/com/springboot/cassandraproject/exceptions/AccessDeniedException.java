package com.springboot.cassandraproject.exceptions;

public class AccessDeniedException extends RuntimeException{

    public AccessDeniedException() {
        super("Access denied");
    }
}
