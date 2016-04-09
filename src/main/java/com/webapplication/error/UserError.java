package com.webapplication.error;

public enum UserError {
    USER_DOES_NOT_EXIST("User does not exist."),
    INVALID_DATA("Invalid data.");

    private final String description;

    UserError(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
