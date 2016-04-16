package com.webapplication.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQuery(name="Bid.findAll", query="SELECT b FROM Bid b")
public class Bid implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int bidId;

    private double amount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date bidDate;

    @ManyToOne
    @JoinColumn(name="UserId")
    private User user;

    @ManyToOne
    @JoinColumn(name="AuctionItemId")
    private Auctionitem auctionitem;

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

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Auctionitem getAuctionitem() {
        return this.auctionitem;
    }

    public void setAuctionitem(Auctionitem auctionitem) {
        this.auctionitem = auctionitem;
    }

}
