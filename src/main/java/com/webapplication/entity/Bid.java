package com.webapplication.entity;

import org.mongodb.morphia.annotations.Embedded;

import java.util.Date;

@Embedded
public class Bid {

    private Double amount;
    private Date bidDate;
    private String userId;

    public Bid() {
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getBidDate() {
        return bidDate;
    }

    public void setBidDate(Date bidDate) {
        this.bidDate = bidDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
