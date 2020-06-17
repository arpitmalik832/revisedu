package com.revisedu.revised.request;

import com.google.gson.annotations.SerializedName;

public class SubjectRequest {

    @SerializedName("class_id")
    private String mClassId = "";

    public SubjectRequest(String classId) {
        this.mClassId = classId;
    }

}
