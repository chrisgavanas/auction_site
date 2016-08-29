package com.webapplication.exception.user;

import com.webapplication.error.user.UserError;

public class UserAlreadyVerifiedException extends Exception {

    public UserAlreadyVerifiedException(UserError error) {
        super(error.getDescription());
    }

}
