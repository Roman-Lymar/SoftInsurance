package com.springboot.cassandraproject.exceptions;

public class AuthorizationHeaderNotExistsException extends RuntimeException {

    public AuthorizationHeaderNotExistsException() {
        super("Authorization header is empty");
    }
}
