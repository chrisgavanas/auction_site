package com.webapplication.dto.user;


public class VoteLinkDto {

    private String messageId;
    private String auctionItemId;
    private String voteReceiverId;

    public VoteLinkDto() {
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getAuctionItemId() {
        return auctionItemId;
    }

    public void setAuctionItemId(String auctionItemId) {
        this.auctionItemId = auctionItemId;
    }

    public String getVoteReceiverId() {
        return voteReceiverId;
    }

    public void setVoteReceiverId(String voteReceiverId) {
        this.voteReceiverId = voteReceiverId;
    }

}
