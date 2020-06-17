package com.revisedu.revised.request;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {

    @SerializedName("name")
    private String name = "";
    @SerializedName("email")
    private String email = "";
    @SerializedName("password")
    private String password = "";
    @SerializedName("mobile")
    private String mobile = "";
    @SerializedName("area")
    private String area = "";
    @SerializedName("city")
    private String city = "";
    @SerializedName("landmark")
    private String landmark = "";

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }
}
