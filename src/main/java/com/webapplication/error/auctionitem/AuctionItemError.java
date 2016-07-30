package com.webapplication.error.auctionitem;

public enum AuctionItemError {
    MISSING_DATA("Missing data."),
    INVALID_DATA("Invalid data."),
    USER_NOT_FOUND("Invalid user id."),
    AUCTION_ITEM_NOT_FOUND("Invalid auction item id.");

    private final String description;

    AuctionItemError(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
