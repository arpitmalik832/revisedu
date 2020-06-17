package com.revisedu.revised.response;

import com.google.gson.annotations.SerializedName;

public class FetchBannersResponse {

    @SerializedName("errorCode")
    private int mErrorCode;

    @SerializedName("bannerOne")
    private String mBannerOne;

    @SerializedName("bannerTwo")
    private String mBannerTwo;

    public int getErrorCode() {
        return mErrorCode;
    }

    public String getBannerOne() {
        return mBannerOne;
    }

    public String getBannerTwo() {
        return mBannerTwo;
    }
}
