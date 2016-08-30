package com.webapplication.exception.auctionitem;


import com.webapplication.error.auctionitem.AuctionItemError;

public class AuctionDurationTooShortException extends Exception {

    public AuctionDurationTooShortException(AuctionItemError error) {
        super(error.getDescription());
    }

}
