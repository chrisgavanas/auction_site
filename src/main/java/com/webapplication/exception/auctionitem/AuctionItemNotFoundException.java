package com.webapplication.exception.auctionitem;


import com.webapplication.error.auctionitem.AuctionItemError;

public class AuctionItemNotFoundException extends Exception {

    public AuctionItemNotFoundException(AuctionItemError error) {
        super(error.getDescription());
    }

}
