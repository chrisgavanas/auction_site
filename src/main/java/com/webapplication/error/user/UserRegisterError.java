package com.webapplication.error.user;

public enum UserRegisterError {
	MISSING_DATA("Required attributes are missing."),
	INVALID_DATA("Invalid data."),
	USERNAME_ALREADY_IN_USE("Username is already in use."),
	EMAIL_ALREADY_USED("Email is already in use.");
	
	private final String description;
	
	UserRegisterError(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}
