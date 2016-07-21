package com.webapplication.dto.user;

public class GeoLocationDto {

    private Double latitude;
    private Double longitude;

    public GeoLocationDto() {
    }

    public GeoLocationDto(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

}