package com.xmlparser.entity;


import com.xmlparser.entitylist.BidAuctionList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Item")
public class Auction {
    @XmlAttribute(name = "ItemID")
    private Integer auctionItemId;

    @XmlElement(name = "Name")
    private String name;

    @XmlElement(name = "Currently")
    private String currentBid;

    @XmlElement(name = "First_Bid")
    private String minBid;

    @XmlElement(name = "Location")
    private Location location;

    @XmlElement(name = "Number_of_Bids")
    private Integer bidsNo;

    @XmlElement(name = "Bids")
    private BidAuctionList bids;

    @XmlElement(name = "Country")
    private String country;

    @XmlElement(name = "Started")
    private String startDate;

    @XmlElement(name = "Ends")
    private String endDate;

    @XmlElement(name = "Seller")
    private Seller seller;

    @XmlElement(name = "Description")
    private String description;

    @XmlElement(name = "Category")
    private List<String> categories;

    public Integer getAuctionItemId() {
        return auctionItemId;
    }

    public void setAuctionItemId(Integer auctionItemId) {
        this.auctionItemId = auctionItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(String currentBid) {
        this.currentBid = currentBid;
    }

    public String getMinBid() {
        return minBid;
    }

    public void setMinBid(String minBid) {
        this.minBid = minBid;
    }

    public Integer getBidsNo() {
        return bidsNo;
    }

    public void setBidsNo(Integer bidsNo) {
        this.bidsNo = bidsNo;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BidAuctionList getBids() {
        return bids;
    }

    public void setBids(BidAuctionList bids) {
        this.bids = bids;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

}
