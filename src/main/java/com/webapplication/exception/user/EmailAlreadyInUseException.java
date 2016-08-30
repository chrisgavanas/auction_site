package com.webapplication.exception.user;


import com.webapplication.error.user.UserError;

public class EmailAlreadyInUseException extends Exception {

    public EmailAlreadyInUseException(UserError error) {
        super(error.getDescription());
    }

}
