package com.webapplication.exception.user;

import com.webapplication.error.user.UserError;
import com.webapplication.error.user.UserLogInError;
import com.webapplication.error.user.UserRegisterError;

public class ValidationException extends Exception {
    private static final long serialVersionUID = 1L;

    public ValidationException(UserRegisterError error) {
        super(error.getDescription());
    }

    public ValidationException(UserLogInError error) {
        super(error.getDescription());
    }

    public ValidationException(UserError error) {
        super(error.getDescription());
    }

}
