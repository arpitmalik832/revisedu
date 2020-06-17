package com.revisedu.revised.request;

import com.google.gson.annotations.SerializedName;

public class CityRequest {

    @SerializedName("landmark_id")
    private String mLandmarkId = "";

    public CityRequest(String landmarkId) {
        this.mLandmarkId = landmarkId;
    }

}
