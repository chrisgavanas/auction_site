package com.webapplication.error.user;

public enum UserLogInError {
	MISSING_DATA("Username or password is missing."),
	INVALID_DATA("Invalid data."),
	INVALID_CREDENTIALS("Username and password do not match."),
	USER_NOT_EMAIL_VERIFIED("User has not been verified yet.");
	
	private final String description;
	
	UserLogInError( String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
