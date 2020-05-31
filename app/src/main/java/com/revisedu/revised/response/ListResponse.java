package com.revisedu.revised.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ListResponse {

    @SerializedName("list")
    private List<String> mArrayList = new ArrayList<>();

    public List<String> getArrayList() {
        return mArrayList;
    }

}
