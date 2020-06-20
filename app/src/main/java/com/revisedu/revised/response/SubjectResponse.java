package com.revisedu.revised.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SubjectResponse {

    @SerializedName("list")
    private List<ListItem> mArrayList = new ArrayList<>();

    public List<ListItem> getArrayList() {
        return mArrayList;
    }

    public static class ListItem {

        @SerializedName("subject")
        private String mSubjectName;

        public ListItem(String mSubjectName) { //todo remove
            this.mSubjectName = mSubjectName;
        }

        public String getSubjectName() {
            return mSubjectName;
        }
    }
}
