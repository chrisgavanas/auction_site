package com.webapplication.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private int userId;

    private String city;

    private String country;

    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    private String email;

    private String firstName;

    private String gender;

    private boolean isVerified;

	private boolean isAdmin;

    private String lastName;

    private String mobileNumber;

    private String password;

    private String postalCode;

    private float ratingAsBidder;

    private float ratingAsSeller;

    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;

    private String street;

    private String telephoneNumber;

    private String username;

    private String vat;

    @OneToMany(mappedBy = "user")
    private List<Auctionitem> auctionitems;

    @OneToMany(mappedBy = "user")
    private List<Bid> bids;

    public User() {
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean getIsAdmin() {
        return this.isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public boolean getIsVerified() {
        return this.isVerified;
    }

    public void setIsVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNumber() {
        return this.mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public float getRatingAsBidder() {
        return this.ratingAsBidder;
    }

    public void setRatingAsBidder(float ratingAsBidder) {
        this.ratingAsBidder = ratingAsBidder;
    }

    public float getRatingAsSeller() {
        return this.ratingAsSeller;
    }

    public void setRatingAsSeller(float ratingAsSeller) {
        this.ratingAsSeller = ratingAsSeller;
    }

    public Date getRegistrationDate() {
        return this.registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getStreet() {
        return this.street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getTelephoneNumber() {
        return this.telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVat() {
        return this.vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public List<Auctionitem> getAuctionitems() {
        return this.auctionitems;
    }

    public void setAuctionitems(List<Auctionitem> auctionitems) {
        this.auctionitems = auctionitems;
    }

    public Auctionitem addAuctionitem(Auctionitem auctionitem) {
        getAuctionitems().add(auctionitem);
        auctionitem.setUser(this);

        return auctionitem;
    }

    public Auctionitem removeAuctionitem(Auctionitem auctionitem) {
        getAuctionitems().remove(auctionitem);
        auctionitem.setUser(null);

        return auctionitem;
    }

    public List<Bid> getBids() {
        return this.bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public Bid addBid(Bid bid) {
        getBids().add(bid);
        bid.setUser(this);

        return bid;
    }

    public Bid removeBid(Bid bid) {
        getBids().remove(bid);
        bid.setUser(null);

        return bid;
    }

}
