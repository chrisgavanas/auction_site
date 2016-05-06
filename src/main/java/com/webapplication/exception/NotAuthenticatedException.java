package com.webapplication.exception;


import com.webapplication.error.user.UserError;

public class NotAuthenticatedException extends Exception {
    private static final long serialVersionUID = 1L;

    public NotAuthenticatedException(UserError error) {
        super(error.getDescription());
    }
}
