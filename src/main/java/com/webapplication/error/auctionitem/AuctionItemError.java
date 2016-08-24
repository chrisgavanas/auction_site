package com.webapplication.error.auctionitem;

public enum AuctionItemError {
    MISSING_DATA("Missing data."),
    INVALID_DATA("Invalid data."),
    USER_NOT_FOUND("Invalid user id."),
    AUCTION_ITEM_NOT_FOUND("Invalid auction item id."),
    AUCTION_ALREADY_IN_PROGRESS("Auction has been already started."),
    INVALID_AUCTION_END_DATE("End date of the auction must be after current date."),
    AUCTION_DURATION_TOO_SHORT("End date of the auction must last at least 1 hour."),
    INVALID_PAGINATION_VALUES("\"From\" value must be greater or equal to \"To\" value"),
    INVALID_AUCTION("Auction belongs to another user.");

    private final String description;

    AuctionItemError(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
