package com.webapplication.exception;


import com.webapplication.error.user.UserError;
import com.webapplication.error.user.UserLogInError;

public class NotAuthenticatedException extends Exception {

    public NotAuthenticatedException(UserError error) {
        super(error.getDescription());
    }

    public NotAuthenticatedException(UserLogInError error) {
        super(error.getDescription());
    }

}
