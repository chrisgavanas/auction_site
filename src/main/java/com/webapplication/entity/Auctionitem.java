package com.webapplication.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.webapplication.dto.user.GeoLocationDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@XmlRootElement(name = "Item")
@Entity
@NamedQuery(name = "Auctionitem.findAll", query = "SELECT a FROM Auctionitem a")
public class Auctionitem implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private GeoLocationDto geoLocationDto = new GeoLocationDto();

    public GeoLocationDto getGeoLocationDto() {
        geoLocationDto.setLatitude(latitude);
        geoLocationDto.setLongitude(longitude);
        return geoLocationDto;
    }

    public void setGeoLocationDto(GeoLocationDto geoLocationDto) {
        if (geoLocationDto != null) {
            this.latitude = geoLocationDto.getLatitude();
            this.longitude = geoLocationDto.getLongitude();
        }
    }

    @ManyToOne
    @JoinColumn(name = "UserId")
    private User user;

    @ManyToMany(mappedBy = "auctionitems")
    @JsonIgnore
    private List<Category> categories;

    @OneToMany(mappedBy = "auctionitem")
    private List<Bid> bids;

    @OneToMany(mappedBy = "auctionitem")
    private List<Image> images;

    public Auctionitem() {
    }

    public Integer getAuctionItemId() {
        return this.auctionItemId;
    }

    @XmlAttribute(name = "ItemID")
    public void setAuctionItemId(Integer auctionItemId) {
        this.auctionItemId = auctionItemId;
    }

    public Integer getBidsNo() {
        return this.bidsNo;
    }

    @XmlElement(name = "Number_of_Bids")
    public void setBidsNo(Integer bidsNo) {
        this.bidsNo = bidsNo;
    }

    public Double getBuyout() {
        return this.buyout;
    }

    @XmlElement(name = "Buy_Price")
    public void setBuyout(Double buyout) {
        this.buyout = buyout;
    }

    public Double getCurrentBid() {
        return this.currentBid;
    }

    @XmlElement(name = "Currently")
    public void setCurrentBid(Double currentBid) {
        this.currentBid = currentBid;
    }

    public String getDescription() {
        return this.description;
    }

    @XmlElement(name = "Description")
    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    @XmlElement(name = "Ends")
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getLatitude() {
        return this.latitude;
    }

//    @XmlElement(name = "")
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

//    @XmlElement(name = "")
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getMinBid() {
        return this.minBid;
    }

    @XmlElement(name = "First_Bid")
    public void setMinBid(Double minBid) {
        this.minBid = minBid;
    }

    public String getName() {
        return this.name;
    }

    @XmlElement(name = "Name")
    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    @XmlElement(name = "Started")
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

//    @XmlElementWrapper(name="email-addresses")
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

    @Override
    public String toString() {
        return "Auctionitem{" +
                "auctionItemId=" + auctionItemId + "\n" +
                ", bidsNo=" + bidsNo + "\n" +
                ", buyout=" + buyout + "\n" +
                ", currentBid=" + currentBid + "\n" +
                ", description='" + description + '\'' + "\n" +
                ", endDate=" + endDate + "\n" +
                ", latitude=" + latitude + "\n" +
                ", longitude=" + longitude + "\n" +
                ", minBid=" + minBid + "\n" +
                ", name='" + name + '\'' + "\n" +
                ", startDate=" + startDate + "\n" +
                ", geoLocationDto=" + geoLocationDto + "\n" +
                ", user=" + user + "\n" +
                ", categories=" + categories + "\n" +
                ", bids=" + bids + "\n" +
                ", images=" + images + "\n" +
                '}';
    }
}
