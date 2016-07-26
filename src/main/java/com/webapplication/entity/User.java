package com.webapplication.entity;

import com.webapplication.dto.user.Gender;
import org.mongodb.morphia.annotations.Embedded;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;


@Document(collection = "User")
public class User {

    @Id
    private String userId;
    private String country;
    private Date dateOfBirth;
    private String email;
    private String firstName;
    private Gender gender;
    private Boolean isAdmin;
    private Boolean isVerified;
    private String lastName;
    private String mobileNumber;
    private String password;
    private String phoneNumber;
    private Integer ratingAsBidder;
    private Integer ratingAsSeller;
    private Date registrationDate;
    private String username;
    private String vat;

    @Embedded
    private Address address;

    private List<String> auctionItemIds;
    private List<String> bidIds;

    public User() {
    }

    public User(String username, Integer ratingAsSeller, Boolean isVerified) {     //Needed for XML unmarshall/unmarshall
        this.username = username;
        this.ratingAsSeller = ratingAsSeller;
        this.isVerified = isVerified;
        this.isAdmin = false;
    }

    public User(String username, Integer ratingAsBidder, String country, Address address, Boolean isVerified) { //Needed for XML unmarshall/unmarshall
        this.username = username;
        this.ratingAsBidder = ratingAsBidder;
        this.country = country;
        this.address = address;
        this.isVerified = isVerified;
        this.isAdmin = false;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getRatingAsBidder() {
        return ratingAsBidder;
    }

    public void setRatingAsBidder(Integer ratingAsBidder) {
        this.ratingAsBidder = ratingAsBidder;
    }

    public Integer getRatingAsSeller() {
        return ratingAsSeller;
    }

    public void setRatingAsSeller(Integer ratingAsSeller) {
        this.ratingAsSeller = ratingAsSeller;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public List<String> getAuctionItemIds() {
        return auctionItemIds;
    }

    public void setAuctionItemIds(List<String> auctionItemIds) {
        this.auctionItemIds = auctionItemIds;
    }

    public List<String> getBidIds() {
        return bidIds;
    }

    public void setBidIds(List<String> bidIds) {
        this.bidIds = bidIds;
    }

}
