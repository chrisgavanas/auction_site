package com.webapplication.exception.user;


import com.webapplication.error.user.UserError;

public class VoteException extends Exception {

    public VoteException(UserError error) {
        super(error.getDescription());
    }

}
