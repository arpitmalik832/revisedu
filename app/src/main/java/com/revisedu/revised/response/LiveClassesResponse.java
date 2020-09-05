package com.revisedu.revised.response;

import java.util.List;

public class LiveClassesResponse {

    private List<LiveClassesResponseItem> liveClasses;

    public List<LiveClassesResponseItem> getLiveClasses() { return liveClasses; }

    public static class LiveClassesResponseItem {

        private String thumbnail;
        private String name;

        public LiveClassesResponseItem(String thumbnail, String name) {
            this.thumbnail = thumbnail;
            this.name = name;
        }

        public String getThumbnail() { return thumbnail; }

        public String getName() { return name; }

    }
}
