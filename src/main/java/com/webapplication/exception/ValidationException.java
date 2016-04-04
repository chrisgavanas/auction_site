package com.webapplication.exception;

import com.webapplication.error.UserLogInError;
import com.webapplication.error.UserRegisterError;

public class ValidationException extends Exception {
	private static final long serialVersionUID = 1L;

	public ValidationException(UserRegisterError error) {
		super(error.getDescription());
	}
	
	public ValidationException(UserLogInError error) {
		super(error.getDescription());
	}
}
