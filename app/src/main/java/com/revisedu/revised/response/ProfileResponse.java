package com.revisedu.revised.response;

import com.google.gson.annotations.SerializedName;

public class ProfileResponse extends CommonResponse {

    @SerializedName("name")
    private String name = "";
    @SerializedName("email")
    private String email = "";
    @SerializedName("pref_subjects")
    private String prefSubjects = "";
    @SerializedName("mobile")
    private String mobile = "";
    @SerializedName("userImageUrl")
    private String userImageUrl = "";

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPrefSubjects() {
        return prefSubjects;
    }

    public String getMobile() {
        return mobile;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }
}
