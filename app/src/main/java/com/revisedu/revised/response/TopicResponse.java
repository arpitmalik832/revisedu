package com.revisedu.revised.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TopicResponse {

    @SerializedName("list")
    private List<ListItem> mArrayList = new ArrayList<>();

    public List<ListItem> getArrayList() {
        return mArrayList;
    }

    public static class ListItem {

        @SerializedName("topic")
        private String mTopicName;

        public ListItem(String mSubjectName) { //todo remove
            this.mTopicName = mSubjectName;
        }

        public String getTopicName() {
            return mTopicName;
        }
    }
}
