package com.webapplication.exception;


import com.webapplication.error.user.UserError;

public class NotAuthenticatedException extends Exception {

    public NotAuthenticatedException(UserError error) {
        super(error.getDescription());
    }

}
