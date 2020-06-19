package com.revisedu.revised.request;

import com.google.gson.annotations.SerializedName;

public class TutorRequest {

    @SerializedName("mode")
    private String mMode = "";
    @SerializedName("userId")
    private String mUserId = "";
    @SerializedName("index")
    private int mIndex;

    public TutorRequest(String mode, int index, String userId) {
        this.mMode = mode;
        this.mIndex = index;
        this.mUserId = userId;
    }
}
