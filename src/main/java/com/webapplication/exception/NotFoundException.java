package com.webapplication.exception;

import com.webapplication.error.UserLogInError;
import com.webapplication.error.UserRegisterError;

public class NotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public NotFoundException(UserRegisterError error) {
		super(error.getDescription());
	}

	public NotFoundException(UserLogInError error) {
		super(error.getDescription());
	}
}
