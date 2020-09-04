package com.revisedu.revised.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CoachingResponse extends CommonResponse {

    @SerializedName("list")
    private List<CoachingResponseItem> mArrayList = new ArrayList<>();

    public List<CoachingResponseItem> getArrayList() { return mArrayList; }

    public boolean isDataAvailable() { return !mArrayList.isEmpty(); }

    public static class CoachingResponseItem {
        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("image")
        private String image;

        public String getId() { return id; }

        public String getName() { return name; }

        public String getImage() { return image; }
    }
}
