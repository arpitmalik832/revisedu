package com.revisedu.revised.request;

import com.google.gson.annotations.SerializedName;

public class DetailRequest {

    @SerializedName("mode")
    private String mMode = "";

    public DetailRequest(String mode) {
        mMode = mode;
    }
}
