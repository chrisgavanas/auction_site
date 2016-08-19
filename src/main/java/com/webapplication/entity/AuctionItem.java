package com.webapplication.entity;

import org.mongodb.morphia.annotations.Embedded;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "AuctionItem")
public class AuctionItem {

    @Id
    private String auctionItemId;
    private Integer bidsNo;
    private Double buyout;
    private Double currentBid;
    private String description;
    private Date endDate;
    private Double minBid;
    private String name;
    private Date startDate;
    private String userId;
    private List<String> categoriesId;
    private List<String> images;

    @Embedded
    private GeoLocation geoLocation;

    @Embedded
    private List<Bid> bids;

    public AuctionItem() {
    }

    public String getAuctionItemId() {
        return this.auctionItemId;
    }

    public void setAuctionItemId(String auctionItemId) {
        this.auctionItemId = auctionItemId;
    }

    public Integer getBidsNo() {
        return this.bidsNo;
    }

    public void setBidsNo(Integer bidsNo) {
        this.bidsNo = bidsNo;
    }

    public Double getBuyout() {
        return this.buyout;
    }

    public void setBuyout(Double buyout) {
        this.buyout = buyout;
    }

    public Double getCurrentBid() {
        return this.currentBid;
    }

    public void setCurrentBid(Double currentBid) {
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

    public Double getMinBid() {
        return this.minBid;
    }

    public void setMinBid(Double minBid) {
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

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public List<String> getCategoriesId() {
        return categoriesId;
    }

    public void setCategories(List<String> categoriesId) {
        this.categoriesId = categoriesId;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

}
