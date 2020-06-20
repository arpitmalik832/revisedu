package com.revisedu.revised.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ClassResponse {

    @SerializedName("list")
    private List<ListItem> mArrayList = new ArrayList<>();

    public List<ListItem> getArrayList() {
        return mArrayList;
    }

    public static class ListItem {

        @SerializedName("class")
        private String className;

        public ListItem(String className) { //todo remove
            this.className = className;
        }

        public String getClassName() {
            return className;
        }
    }
}
