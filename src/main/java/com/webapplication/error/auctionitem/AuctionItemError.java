package com.webapplication.error.auctionitem;

public enum AuctionItemError {
    MISSING_DATA("Missing data."),
    INVALID_DATA("Invalid data."),
    USER_NOT_FOUND("Invalid user id."),
    AUCTION_ITEM_NOT_FOUND("Invalid auction item id."),
    AUCTION_ALREADY_IN_PROGRESS("Auction has been already started."),
    AUCTION_EXPIRED("Auction has been expired."),
    INVALID_AUCTION_END_DATE("End date of the auction must be after current date."),
    AUCTION_DURATION_TOO_SHORT("End date of the auction must last at least 1 hour."),
    INVALID_PAGINATION_VALUES("\"From\" value must be greater or equal to \"To\" value"),
    BID_AMOUNT_ABOVE_BUYOUT("Bid amount would result to a price above buyout amount"),
    BID_AMOUNT_BELOW_ALLOWED_AMOUNT("Bid amount is lower than the minimum amount allowed for bidding this auction item"),
    AUCTION_HAS_NOT_STARTED("Auction has not started yet."),
    AUCTION_HAS_BEEN_COMPLETED("Auction has expired."),
    ITEM_BELONGS_TO_BIDDER("Bidder can't own the auction item"),
    UNAUTHORIZED("Unauthorized action."),
    INVALID_BUYOUT_FROM_USER("This user can't buyout this item"),
    IMAGE_DOES_NOT_EXIST("Image was not found.");

    private final String description;

    AuctionItemError(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
