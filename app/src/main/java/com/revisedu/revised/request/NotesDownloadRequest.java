package com.revisedu.revised.request;

import com.google.gson.annotations.SerializedName;

public class NotesDownloadRequest extends CommonRequest{

    @SerializedName("class")
    private String classStr = "";
    @SerializedName("subject")
    private String subjectStr = "";
    @SerializedName("topic")
    private String topicStr = "";

    public void setClassStr(String classStr) {
        this.classStr = classStr;
    }

    public void setSubjectStr(String subjectStr) {
        this.subjectStr = subjectStr;
    }

    public void setTopicStr(String topicStr) {
        this.topicStr = topicStr;
    }
}
