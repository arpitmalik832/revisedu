package com.revisedu.revised.request;

import com.google.gson.annotations.SerializedName;

public class TutorRequest {

    @SerializedName("mode")
    private String mMode = "";
    @SerializedName("index")
    private int mIndex;

    public TutorRequest(String mode, int index) {
        this.mMode = mode;
        this.mIndex = index;
    }
}
