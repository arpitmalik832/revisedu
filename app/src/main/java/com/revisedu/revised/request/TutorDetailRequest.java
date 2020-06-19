package com.revisedu.revised.request;

import com.google.gson.annotations.SerializedName;

public class TutorDetailRequest {

    @SerializedName("tutorId")
    private String tutorId = "";

    public TutorDetailRequest(String tutorId) {
        this.tutorId = tutorId;
    }
}
