package com.webapplication.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

@Entity
@NamedQuery(name = "Bid.findAll", query = "SELECT b FROM Bid b")
public class Bid implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private int bidId;

    private double amount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date bidDate;

    @ManyToOne
    @JoinColumn(name = "SellerId")
    private Auctionitem auctionitem;

    @ManyToOne
    @JoinColumn(name = "UserId")
    private User user;

    public Bid() {
    }

    public int getBidId() {
        return this.bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getBidDate() {
        return this.bidDate;
    }

    public void setBidDate(Date bidDate) {
        this.bidDate = bidDate;
    }

    public Auctionitem getAuctionitem() {
        return this.auctionitem;
    }

    public void setAuctionitem(Auctionitem auctionitem) {
        this.auctionitem = auctionitem;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
