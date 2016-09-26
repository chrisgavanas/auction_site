package com.webapplication.dto.user;


public class VoteLinkDto {

    private String auctionItemId;
    private String voteReceiverId;

    public VoteLinkDto(String auctionItemId, String voteReceiverId) {
        this.auctionItemId = auctionItemId;
        this.voteReceiverId = voteReceiverId;
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
