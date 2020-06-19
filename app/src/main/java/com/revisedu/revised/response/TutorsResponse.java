package com.revisedu.revised.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TutorsResponse extends CommonResponse {

    @SerializedName("list")
    private List<TutorsResponseItem> mArrayList = new ArrayList<>();
    @SerializedName("isDataAvailable")
    private boolean mIsDataAvailable;

    public void setArrayList(List<TutorsResponseItem> arrayList) {
        mArrayList = arrayList;
    }

    public boolean isDataAvailable() {
        return mIsDataAvailable;
    }

    public void setDataAvailable(boolean dataAvailable) {
        mIsDataAvailable = dataAvailable;
    }

    public List<TutorsResponseItem> getArrayList() {
        return mArrayList;
    }

    public static class TutorsResponseItem {

        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("image")
        private String image;
        @SerializedName("rating")
        private String rating;
        @SerializedName("location")
        private String location;
        @SerializedName("discount")
        private String discount;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getImage() {
            return image;
        }

        public String getRating() {
            return rating;
        }

        public String getLocation() {
            return location;
        }

        public String getDiscount() {
            return discount;
        }
    }
}
