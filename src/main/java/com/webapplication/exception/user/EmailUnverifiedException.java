package com.webapplication.exception.user;

import com.webapplication.error.user.UserLogInError;
import com.webapplication.error.user.UserRegisterError;

public class EmailUnverifiedException extends Exception {

    public EmailUnverifiedException(UserRegisterError error) {
        super(error.getDescription());
    }

    public EmailUnverifiedException(UserLogInError error) {
        super(error.getDescription());
    }
}
