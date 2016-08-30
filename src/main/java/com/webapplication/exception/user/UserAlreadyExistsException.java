package com.webapplication.exception.user;

import com.webapplication.error.user.UserError;
import com.webapplication.error.user.UserLogInError;
import com.webapplication.error.user.UserRegisterError;

public class UserAlreadyExistsException extends Exception {

	public UserAlreadyExistsException(UserRegisterError error) {
		super(error.getDescription());
	}

	public UserAlreadyExistsException(UserLogInError error) {
		super(error.getDescription());
	}

	public UserAlreadyExistsException(UserError error) {
		super(error.getDescription());
	}

}
