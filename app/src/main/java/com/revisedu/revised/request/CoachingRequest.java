package com.revisedu.revised.request;

import com.google.gson.annotations.SerializedName;

public class CoachingRequest {

    @SerializedName("package_id")
    private String packageId;

    public CoachingRequest(String packageId) {
        this.packageId = packageId;
    }

    @SerializedName("mode")
    private String mMode = "";
    @SerializedName("userId")
    private String mUserId = "";
    @SerializedName("subject")
    private String mSubject = "";
    @SerializedName("location")
    private String mLocation = "";
    @SerializedName("city")
    private String mCity = "";
    @SerializedName("class")
    private String mClass = "";
    @SerializedName("index")
    private int mIndex;

    public String getSubject() {
        return mSubject;
    }

    public void setSubject(String mSubject) {
        this.mSubject = mSubject;
    }

    public void setSearchParams(SearchRequestModel searchRequestModel){
        this.mCity = searchRequestModel.getCity();
        this.mLocation = searchRequestModel.getLocation();
        this.mClass = searchRequestModel.getMclass();
        this.mSubject = searchRequestModel.getSubject();
    }
}
