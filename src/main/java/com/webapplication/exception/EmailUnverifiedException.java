package com.webapplication.exception;

import com.webapplication.error.user.UserLogInError;
import com.webapplication.error.user.UserRegisterError;

public class EmailUnverifiedException extends Exception {
	private static final long serialVersionUID = 1L;

	public EmailUnverifiedException(UserRegisterError error) {
		super(error.getDescription());
	}

	public EmailUnverifiedException(UserLogInError error) {
		super(error.getDescription());
	}
}
