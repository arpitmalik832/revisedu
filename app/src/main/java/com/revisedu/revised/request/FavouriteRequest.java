package com.revisedu.revised.request;

import com.google.gson.annotations.SerializedName;

public class FavouriteRequest {

    @SerializedName("userId")
    private String userId = "";
    @SerializedName("tutorId")
    private String tutorId = "";
    @SerializedName("isFavourite")
    private boolean isFavourite;

    public FavouriteRequest(String userId, String tutorId, boolean isFavourite) {
        this.userId = userId;
        this.tutorId = tutorId;
        this.isFavourite = isFavourite;
    }
}
