package com.webapplication.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@NamedQuery(name="Auctionitem.findAll", query="SELECT a FROM Auctionitem a")
public class Auctionitem implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private int auctionItemId;

    private int bidsNo;

    private double buyout;

    private double currentBid;

    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    private double latitude;

    private double longitude;

    private double minBid;

    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @ManyToOne
    @JoinColumn(name="UserId")
    private User user;

    @OneToMany(mappedBy="auctionitem")
    private List<Bid> bids;

    @ManyToMany(mappedBy="auctionitems")
    private List<Category> categories;

    //bi-directional many-to-one association to Image
    @OneToMany(mappedBy="auctionitem")
    private List<Image> images;

    public Auctionitem() {
    }

    public int getAuctionItemId() {
        return this.auctionItemId;
    }

    public void setAuctionItemId(int auctionItemId) {
        this.auctionItemId = auctionItemId;
    }

    public int getBidsNo() {
        return this.bidsNo;
    }

    public void setBidsNo(int bidsNo) {
        this.bidsNo = bidsNo;
    }

    public double getBuyout() {
        return this.buyout;
    }

    public void setBuyout(double buyout) {
        this.buyout = buyout;
    }

    public double getCurrentBid() {
        return this.currentBid;
    }

    public void setCurrentBid(double currentBid) {
        this.currentBid = currentBid;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getMinBid() {
        return this.minBid;
    }

    public void setMinBid(double minBid) {
        this.minBid = minBid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Bid> getBids() {
        return this.bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public Bid addBid(Bid bid) {
        getBids().add(bid);
        bid.setAuctionitem(this);

        return bid;
    }

    public Bid removeBid(Bid bid) {
        getBids().remove(bid);
        bid.setAuctionitem(null);

        return bid;
    }

    public List<Category> getCategories() {
        return this.categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Image> getImages() {
        return this.images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Image addImage(Image image) {
        getImages().add(image);
        image.setAuctionitem(this);

        return image;
    }

    public Image removeImage(Image image) {
        getImages().remove(image);
        image.setAuctionitem(null);

        return image;
    }

}
