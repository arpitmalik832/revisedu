package com.revisedu.revised.request;

import com.google.gson.annotations.SerializedName;

public class LandmarkRequest {

    @SerializedName("area")
    private String area = "";

    public LandmarkRequest(String area) {
        this.area = area;
    }

}
