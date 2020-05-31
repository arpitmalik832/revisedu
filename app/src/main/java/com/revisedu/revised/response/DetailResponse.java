package com.revisedu.revised.response;

import com.google.gson.annotations.SerializedName;

public class DetailResponse {

    @SerializedName("errorCode")
    private String mErrorCode = "";
    @SerializedName("errorMessage")
    private String mErrorMessage = "";

    public String getErrorCode() {
        return mErrorCode;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }
}
