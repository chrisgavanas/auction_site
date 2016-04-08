package com.webapplication.error;

public enum UserIdError {
	NOT_EXISTS("User does not exist.");
	
	private final String description;
	
	UserIdError(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}