package com.webapplication.dto.user;

import java.util.Date;

public class SellerResponseDto {

    private String sellerId;
    private String username;
    private String country;
    private Date registrationDate;
    private Gender gender;
    private Integer ratingAsSeller;
    private Integer ratingAsBidder;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

   
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    
    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

   
    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

 

    public Integer getRatingAsSeller() {
        return ratingAsSeller;
    }

    public void setRatingAsSeller(Integer ratingAsSeller) {
        this.ratingAsSeller = ratingAsSeller;
    }

    public Integer getRatingAsBidder() {
        return ratingAsBidder;
    }

    public void setRatingAsBidder(Integer ratingAsBidder) {
        this.ratingAsBidder = ratingAsBidder;
    }

}