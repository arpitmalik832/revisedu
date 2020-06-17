package com.revisedu.revised.response;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("errorCode")
    private int mErrorCode;
    @SerializedName("errorMessage")
    private String mErrorMessage = "";
    @SerializedName("userId")
    private String userId = "";

    public int getErrorCode() {
        return mErrorCode;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public String getUserId() {
        return userId;
    }
}
