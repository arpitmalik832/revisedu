package com.revisedu.revised.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class NotificationsResponse {

    @SerializedName("notificationList")
    private List<NotificationsResponse.ListItem> mArrayList = new ArrayList<>();

    public List<NotificationsResponse.ListItem> getArrayList() {
        return mArrayList;
    }

    public static class ListItem {

        @SerializedName("id")
        private String id;
        @SerializedName("time")
        private String time;
        @SerializedName("msg")
        private String msg;

        public ListItem(String id, String time, String msg) {
            this.id = id;
            this.time = time;
            this.msg = msg;
        }

        public String getId() {
            return id;
        }

        public String getTime() {
            return time;
        }

        public String getMsg() { return msg; }

    }


}
