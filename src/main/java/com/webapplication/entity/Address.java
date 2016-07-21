package com.webapplication.entity;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class Address {

    private String city;
    private String postalCode;
    private String street;

    public Address() {
    }

    public Address(String city, String postalCode, String street) {
        this.city = city;
        this.postalCode = postalCode;
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
