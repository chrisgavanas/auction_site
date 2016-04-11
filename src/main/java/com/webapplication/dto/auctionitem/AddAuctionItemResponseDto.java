package com.webapplication.dto.auctionitem;

import java.util.Date;
import java.util.List;

import com.webapplication.entity.Category;
import com.webapplication.entity.Image;

public class AddAuctionItemResponseDto {

    private Integer auctionItemId;
    private String name;
    private Double currentBid;
    private Double buyout;
    private Double minBid;
    private Integer bidsNo;
    private Date startDate;
    private Date endDate;
    private String description;
    private Double latitude;
    private Double longitutde;
    private String username;
    private Float ratingAsSeller;
    private List<Category> categories;
    private List<Image> images;

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

    public Double getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(Double currentBid) {
        this.currentBid = currentBid;
    }

    public Double getBuyout() {
        return buyout;
    }

    public void setBuyout(Double buyout) {
        this.buyout = buyout;
    }

    public Double getMinBid() {
        return minBid;
    }

    public void setMinBid(Double minBid) {
        this.minBid = minBid;
    }

    public Integer getBidsNo() {
        return bidsNo;
    }

    public void setBidsNo(Integer bidsNo) {
        this.bidsNo = bidsNo;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitutde() {
        return longitutde;
    }

    public void setLongitutde(Double longitutde) {
        this.longitutde = longitutde;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Float getRatingAsSeller() {
        return ratingAsSeller;
    }

    public void setRatingAsSeller(Float ratingAsSeller) {
        this.ratingAsSeller = ratingAsSeller;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

}
