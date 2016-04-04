package com.webapplication.exception;

import com.webapplication.error.UserLogInError;
import com.webapplication.error.UserRegisterError;

public class UserAlreadyExists extends Exception {
	private static final long serialVersionUID = 1L;

	public UserAlreadyExists(UserRegisterError error) {
		super(error.getDescription());
	}

	public UserAlreadyExists(UserLogInError error) {
		super(error.getDescription());
	}
}
