package com.webapplication.exception.auctionitem;


import com.webapplication.error.auctionitem.AuctionItemError;

public class AuctionExpiredException extends Exception {

    public AuctionExpiredException(AuctionItemError error) {
        super(error.getDescription());
    }

}
