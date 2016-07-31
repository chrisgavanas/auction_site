package com.webapplication.exception;


import com.webapplication.error.auctionitem.AuctionItemError;

public class AuctionAlreadyInProgressException extends Exception {

    public AuctionAlreadyInProgressException(AuctionItemError error) {
        super(error.getDescription());
    }

}
