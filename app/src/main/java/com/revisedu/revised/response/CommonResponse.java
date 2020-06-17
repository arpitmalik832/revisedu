package com.revisedu.revised.response;

import com.google.gson.annotations.SerializedName;

public class CommonResponse {

    @SerializedName("errorCode")
    private int mErrorCode;
    @SerializedName("errorMessage")
    private String mErrorMessage = "";

    public int getErrorCode() {
        return mErrorCode;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }
}
