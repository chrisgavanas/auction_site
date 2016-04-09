package com.webapplication.exception.user;

import com.webapplication.error.user.UserLogInError;
import com.webapplication.error.user.UserRegisterError;

public class UserAlreadyExistsException extends Exception {
	private static final long serialVersionUID = 1L;

	public UserAlreadyExistsException(UserRegisterError error) {
		super(error.getDescription());
	}

	public UserAlreadyExistsException(UserLogInError error) {
		super(error.getDescription());
	}
}
