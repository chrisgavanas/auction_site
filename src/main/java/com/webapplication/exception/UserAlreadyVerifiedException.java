package com.webapplication.exception;

import com.webapplication.error.user.UserError;

public class UserAlreadyVerifiedException extends Exception {
    private static final long serialVersionUID = 1L;

    public UserAlreadyVerifiedException(UserError error) {
        super(error.getDescription());
    }

}
