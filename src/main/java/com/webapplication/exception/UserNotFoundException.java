package com.webapplication.exception;

import com.webapplication.error.UserError;
import com.webapplication.error.UserLogInError;
import com.webapplication.error.UserRegisterError;

public class UserNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public UserNotFoundException(UserRegisterError error) {
		super(error.getDescription());
	}

	public UserNotFoundException(UserLogInError error) {
		super(error.getDescription());
	}
	
	public UserNotFoundException(UserError error){
		super(error.getDescription());
	}
}
