package com.pasha.springboot.cassandraproject.exceptions;

public class UnableDeleteProductException extends RuntimeException{
    public UnableDeleteProductException(String message) {
        super(message);
    }
}
