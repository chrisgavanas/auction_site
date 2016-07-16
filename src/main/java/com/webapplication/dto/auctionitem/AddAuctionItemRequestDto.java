package com.webapplication.dto.auctionitem;

import com.webapplication.dto.user.GeoLocationDto;

import java.util.Date;
import java.util.List;

public class AddAuctionItemRequestDto {

    private String name;
    private Double minBid;
    private Double buyout;
    private Date startDate;
    private Date endDate;
    private String description;
    private GeoLocationDto geoLocationDto;
    private Integer userId;
    private List<Integer> categories;
    private List<Byte[]> images;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMinBid() {
        return minBid;
    }

    public void setMinBid(Double minBid) {
        this.minBid = minBid;
    }

    public Double getBuyout() {
        return buyout;
    }

    public void setBuyout(Double buyout) {
        this.buyout = buyout;
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

    public GeoLocationDto getGeoLocationDto() {
        return geoLocationDto;
    }

    public void setGeoLocationDto(GeoLocationDto geoLocationDto) {
        this.geoLocationDto = geoLocationDto;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<Integer> getCategories() {
        return categories;
    }

    public void setCategories(List<Integer> categories) {
        this.categories = categories;
    }

    public List<Byte[]> getImages() {
        return images;
    }

    public void setImages(List<Byte[]> images) {
        this.images = images;
    }

}
