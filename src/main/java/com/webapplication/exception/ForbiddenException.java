package com.webapplication.exception;


import com.webapplication.error.user.UserError;
import com.webapplication.error.user.UserLogInError;

public class ForbiddenException extends Exception {

    public ForbiddenException(UserError error) {
        super(error.getDescription());
    }

    public ForbiddenException(UserLogInError error) {
        super(error.getDescription());
    }
}
