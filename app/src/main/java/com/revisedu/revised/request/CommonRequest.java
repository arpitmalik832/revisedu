package com.revisedu.revised.request;

import com.google.gson.annotations.SerializedName;

public class CommonRequest {

    @SerializedName("userId")
    private String userId = "";

    public CommonRequest(String mode) {
        userId = mode;
    }
}
