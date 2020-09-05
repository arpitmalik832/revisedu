package com.revisedu.revised.request;

import com.google.gson.annotations.SerializedName;

public class CoachingDetailRequest {

    @SerializedName("institute_id")
    private String coachingId = "";

    public CoachingDetailRequest(String coachingId) { this.coachingId = coachingId; }
}
