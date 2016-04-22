package com.webapplication.error.user;

public enum UserError {
    USER_DOES_NOT_EXIST("User does not exist."),
    INVALID_DATA("Invalid data."),
    MISSING_DATA("Missing data."),
    USER_ALREADY_VERIFIED("User is already verified."),
    EMAIL_ALREADY_IN_USE("Email is already in use.");

    private final String description;

    UserError(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
