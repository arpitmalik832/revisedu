package com.revisedu.revised.request;

import com.google.gson.annotations.SerializedName;

public class TutorRequest {

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

    public TutorRequest(String mode, int index, String userId) {
        this.mMode = mode;
        this.mIndex = index;
        this.mUserId = userId;
    }

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
