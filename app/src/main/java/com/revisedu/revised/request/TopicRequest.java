package com.revisedu.revised.request;

import com.google.gson.annotations.SerializedName;

public class TopicRequest {

    @SerializedName("class")
    private String mClassId = "";
    @SerializedName("subject")
    private String mSubjectName = "";

    public TopicRequest(String classId, String subject) {
        this.mClassId = classId;
        this.mSubjectName = subject;
    }
}
