package com.webapplication.exception.user;


import com.webapplication.error.user.UserError;

public class MessageNotFoundException extends Exception {

    public MessageNotFoundException(UserError userError) {
        super(userError.getDescription());
    }

}
