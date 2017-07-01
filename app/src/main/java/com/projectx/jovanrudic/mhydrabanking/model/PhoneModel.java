package com.projectx.jovanrudic.mhydrabanking.model;

/**
 * Created by jovanrudic on 6/30/17.
 */

public class PhoneModel {

    String imei;
    String phoneNumber;
    String phoneModel;
    String os;

    public PhoneModel(String imei, String phoneNumber, String phoneModel, String os) {
        this.imei = imei;
        this.phoneNumber = phoneNumber;
        this.phoneModel = phoneModel;
        this.os = os;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }
}
