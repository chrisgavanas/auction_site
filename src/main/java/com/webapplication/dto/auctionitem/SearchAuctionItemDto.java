package com.webapplication.dto.auctionitem;


public class SearchAuctionItemDto {

    private String text;
    private String categoryId;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

}
