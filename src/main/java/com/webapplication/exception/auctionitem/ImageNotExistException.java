package com.webapplication.exception.auctionitem;


import com.webapplication.error.auctionitem.AuctionItemError;

public class ImageNotExistException extends Exception {

    public ImageNotExistException(AuctionItemError error) {
        super(error.getDescription());
    }

}
