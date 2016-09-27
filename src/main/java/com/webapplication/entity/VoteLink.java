package com.webapplication.entity;


public class VoteLink {

    private String auctionItemId;
    private String voterId;
    private String voteReceiverId;
    private Boolean voteUserAsSeller;

    public VoteLink(String auctionItemId, String voterId, String voteReceiverId, Boolean voteUserAsSeller) {
        this.auctionItemId = auctionItemId;
        this.voterId = voterId;
        this.voteReceiverId = voteReceiverId;
        this.voteUserAsSeller = voteUserAsSeller;
    }

    public String getAuctionItemId() {
        return auctionItemId;
    }

    public void setAuctionItemId(String auctionItemId) {
        this.auctionItemId = auctionItemId;
    }

    public String getVoterId() {
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }

    public String getVoteReceiverId() {
        return voteReceiverId;
    }

    public void setVoteReceiverId(String voteReceiverId) {
        this.voteReceiverId = voteReceiverId;
    }

    public Boolean getVoteUserAsSeller() {
        return voteUserAsSeller;
    }

    public void setVoteUserAsSeller(Boolean voteUserAsSeller) {
        this.voteUserAsSeller = voteUserAsSeller;
    }

}
