package com.webapplication.exception.user;

import com.webapplication.error.user.UserError;

public class NotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public NotFoundException(UserError error) {
        super(error.getDescription());
    }

}
