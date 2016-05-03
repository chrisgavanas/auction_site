package com.webapplication.entity;

import com.webapplication.dto.user.AddressDto;
import com.webapplication.dto.user.Gender;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Entity
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;

    private String city;

    private String country;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfBirth;

    private String email;

    private String firstName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Boolean isAdmin;

    private Boolean isVerified;

    private String lastName;

    private String mobileNumber;

    private String password;

    private String phoneNumber;

    private String postalCode;

    private float ratingAsBidder;

    private float ratingAsSeller;

    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;

    private String street;

    private String username;

    private String vat;

    @Transient
    private AddressDto address = new AddressDto();

    public AddressDto getAddress() {
        address.setStreet(street);
        address.setPostalCode(postalCode);
        address.setCity(city);
        return address;
    }

    public void setAddress(AddressDto address) {
        if (address != null) {
            this.street = address.getStreet();
            this.city = address.getCity();
            this.postalCode = address.getPostalCode();
        }
    }

    @OneToMany(mappedBy = "user")
    private List<Bid> bids;

    @OneToMany(mappedBy = "user")
    private List<Auctionitem> auctionitems;

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

    public Gender getGender() {
        return this.gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Boolean getIsAdmin() {
        return this.isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Boolean getIsVerified() {
        return this.isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
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

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

}
