package com.revisedu.revised.request;

import com.google.gson.annotations.SerializedName;

public class BookTutorRequest {

    @SerializedName("tutorId")
    private String tutorId = "";
    @SerializedName("userId")
    private String userId = "";

    public BookTutorRequest(String tutorId, String userId) {
        this.tutorId = tutorId;
        this.userId = userId;
    }
}
