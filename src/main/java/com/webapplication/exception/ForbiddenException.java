package com.webapplication.exception;


import com.webapplication.error.user.UserError;
import com.webapplication.error.user.UserLogInError;

public class ForbiddenException extends Exception {
    private static final long serialVersionUID = 1L;

    public ForbiddenException(UserError error) {
        super(error.getDescription());
    }

    public ForbiddenException(UserLogInError error) {
        super(error.getDescription());
    }
}
