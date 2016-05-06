package com.webapplication.exception;


import com.webapplication.error.user.UserError;

public class ForbiddenException extends Exception {
    private static final long serialVersionUID = 1L;

    public ForbiddenException(UserError error) {
        super(error.getDescription());
    }
}
