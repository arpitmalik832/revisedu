package com.revisedu.revised.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class OffersResponse extends CommonResponse{

    @SerializedName("offerList")
    private List<ListItem> mArrayList = new ArrayList<>();

    public List<ListItem> getArrayList() {
        return mArrayList;
    }

    public static class ListItem {

        @SerializedName("image")
        private String mOfferImage;

        public String getOfferImage() {
            return mOfferImage;
        }

        public void setOfferImage(String offerImage) {
            this.mOfferImage = offerImage;
        }

    }
}
