package com.pasha.springboot.cassandraproject.exceptions;

public enum ErrorMessages {

    NO_RESOURCE_FOUND("Resource with provided id is not found: id="),
    NO_RESOURCE_FOUND_2("No results returned by the Query. No matches with: ");

    private String errorMessage;

    ErrorMessages(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
