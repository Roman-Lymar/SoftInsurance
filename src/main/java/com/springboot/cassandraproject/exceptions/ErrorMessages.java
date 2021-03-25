package com.springboot.cassandraproject.exceptions;

public enum ErrorMessages {

    NO_RESOURCE_FOUND("Resource with provided id is not found: id="),
    NO_RESOURCE_FOUND_BY_NAME("No results returned by the Query. No matches with: "),
    EMPTY_PACKAGE("Package does not have any products. Please, add some!"),
    UNABLE_DELETE_PACKAGE("This products is used by packages");

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
