package com.bitgloomy.server.domain;

public class UserProfile {
    private int uid;
    private String id;
    private String name;
    private String phoneNum;
    private String smsReception;
    private String email;
    private String emailReception;
    private String auth;
    private int point;
    private Address address;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getSmsReception() {
        return smsReception;
    }

    public void setSmsReception(String smsReception) {
        this.smsReception = smsReception;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailReception() {
        return emailReception;
    }

    public void setEmailReception(String emailReception) {
        this.emailReception = emailReception;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
