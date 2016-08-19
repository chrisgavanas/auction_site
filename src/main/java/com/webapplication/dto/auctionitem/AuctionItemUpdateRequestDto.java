package com.webapplication.dto.auctionitem;

import com.webapplication.dto.user.GeoLocationDto;

import java.util.List;

public class AuctionItemUpdateRequestDto {

    private Double buyout;
    private String description;
    private Double minBid;
    private String name;
    private GeoLocationDto geoLocationDto;
    private List<String> categoryIds;
    private List<Byte> images;

    public Double getBuyout() {
        return buyout;
    }

    public void setBuyout(Double buyout) {
        this.buyout = buyout;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getMinBid() {
        return minBid;
    }

    public void setMinBid(Double minBid) {
        this.minBid = minBid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GeoLocationDto getGeoLocationDto() {
        return geoLocationDto;
    }

    public void setGeoLocationDto(GeoLocationDto geoLocationDto) {
        this.geoLocationDto = geoLocationDto;
    }

    public List<String> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<String> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public List<Byte> getImages() {
        return images;
    }

    public void setImages(List<Byte> images) {
        this.images = images;
    }

}
