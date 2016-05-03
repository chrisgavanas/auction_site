package com.webapplication.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.webapplication.dto.user.GeoLocation;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@NamedQuery(name="Auctionitem.findAll", query="SELECT a FROM Auctionitem a")
public class Auctionitem implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int auctionItemId;

    private Integer bidsNo;

    private Double buyout;

    private Double currentBid;

    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    private Double latitude;

    private Double longitude;

    private Double minBid;

    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Transient
    private GeoLocation geoLocation;

    public GeoLocation getGeoLocation() {
        geoLocation.setLatitude(latitude);
        geoLocation.setLongitude(longitude);
        return geoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        if (geoLocation != null) {
            this.latitude = geoLocation.getLatitude();
            this.longitude = geoLocation.getLongitude();
        }
    }

    @ManyToOne
    @JoinColumn(name="UserId")
    private User user;

    @ManyToMany(mappedBy="auctionitems")
    @JsonIgnore
    private List<Category> categories;

    @OneToMany(mappedBy="auctionitem")
    private List<Bid> bids;

    @OneToMany(mappedBy="auctionitem")
    private List<Image> images;

    public Auctionitem() {
    }

    public Integer getAuctionItemId() {
        return this.auctionItemId;
    }

    public void setAuctionItemId(Integer auctionItemId) {
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
    public Double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
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

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public List<Category> getCategories() {
        return this.categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
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
