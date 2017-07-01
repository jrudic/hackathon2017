package com.projectx.jovanrudic.mhydrabanking.model;

/**
 * Created by jovanrudic on 6/30/17.
 */

public class LocationModel {

    private String mLat;
    private String mLong;
    private String mTimeStamp;
    private String addressAvaible;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String knownName;

    public LocationModel(String mLat, String mLong, String mTimeStamp, String addressAvaible, String city, String state, String country, String postalCode, String knownName) {
        this.mLat = mLat;
        this.mLong = mLong;
        this.mTimeStamp = mTimeStamp;
        this.addressAvaible = addressAvaible;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
        this.knownName = knownName;
    }

    public LocationModel(String mLat, String mLong, String mTimeStamp) {
        this.mLat = mLat;
        this.mLong = mLong;
        this.mTimeStamp = mTimeStamp;
    }

    public String getAddressailable() {
        return addressAvaible;
    }

    public void setAddressailable(String addressailable) {
        this.addressAvaible = addressailable;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getKnownName() {
        return knownName;
    }

    public void setKnownName(String knownName) {
        this.knownName = knownName;
    }

    public String getLat() {
        return mLat;
    }

    public void setLat(String lat) {
        this.mLat = mLat;
    }

    public String getLong() {
        return mLong;
    }

    public void setLong(String longitude) {
        this.mLong = mLong;
    }

    public String getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.mTimeStamp = mTimeStamp;
    }
}
