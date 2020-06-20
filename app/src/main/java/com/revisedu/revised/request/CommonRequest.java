package com.revisedu.revised.request;

import com.google.gson.annotations.SerializedName;

public class CommonRequest {

    @SerializedName("userId")
    private String userId = "";

    CommonRequest() {
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public CommonRequest(String mode) {
        userId = mode;
    }
}
