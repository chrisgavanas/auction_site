package com.webapplication.error;

public enum UserLogInError {
	INVALID_CREDENTIALS("1", "Username and password don't match."),
	MISSING_DATA("2", "Username or password is missing."),
	USER_NOT_VERIFIED("3", "User has not been verified yet.");
	
	private final String code;
	private final String description;
	
	UserLogInError(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getDescription() {
		return description;
	}
}
