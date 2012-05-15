package uk.co.mattburns.pwinty.model;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private int id;
    private Status status;
    private List<Photo> photos = new ArrayList<Photo>();
    private String recipientName;
    private String address1;
    private String address2;
    private String addressTownOrCity;
    private String stateOrCounty;
    private String postalOrZipCode;
    private String country;

    public enum Status {
        NotYetSubmitted, Submitted, Complete, Cancelled;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddressTownOrCity() {
        return addressTownOrCity;
    }

    public void setAddressTownOrCity(String addressTownOrCity) {
        this.addressTownOrCity = addressTownOrCity;
    }

    public String getStateOrCounty() {
        return stateOrCounty;
    }

    public void setStateOrCounty(String stateOrCounty) {
        this.stateOrCounty = stateOrCounty;
    }

    public String getPostalOrZipCode() {
        return postalOrZipCode;
    }

    public void setPostalOrZipCode(String postalOrZipCode) {
        this.postalOrZipCode = postalOrZipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Order [id=" + id + ", status=" + status + ", photos=" + photos
                + ", recipientName=" + recipientName + ", address1=" + address1
                + ", address2=" + address2 + ", addressTownOrCity="
                + addressTownOrCity + ", stateOrCounty=" + stateOrCounty
                + ", postalOrZipCode=" + postalOrZipCode + ", country="
                + country + "]";
    }
}
