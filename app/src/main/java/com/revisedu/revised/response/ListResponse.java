package com.revisedu.revised.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ListResponse {

    @SerializedName("list")
    private List<ListItem> mArrayList = new ArrayList<>();

    public List<ListItem> getArrayList() {
        return mArrayList;
    }

    public static class ListItem {

        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
