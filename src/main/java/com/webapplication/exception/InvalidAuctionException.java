package com.webapplication.exception;


import com.webapplication.error.auctionitem.AuctionItemError;

public class InvalidAuctionException extends Exception {

    public InvalidAuctionException(AuctionItemError error) {
        super(error.getDescription());
    }

}
