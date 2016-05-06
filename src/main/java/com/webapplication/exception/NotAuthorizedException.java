package com.webapplication.exception;

import com.webapplication.error.user.UserError;
import com.webapplication.error.user.UserLogInError;
import com.webapplication.error.user.UserRegisterError;

public class NotAuthorizedException extends Exception {
	private static final long serialVersionUID = 1L;

	public NotAuthorizedException(UserError error) {
		super(error.getDescription());
	}

}
