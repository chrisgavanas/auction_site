package com.webapplication.exception.auctionitem;


import com.webapplication.error.auctionitem.AuctionItemError;

public class BuyoutException extends Exception {

    public BuyoutException(AuctionItemError auctionItemError) {
        super(auctionItemError.getDescription());
    }

}
