package com.revisedu.revised.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TutorDetailResponse extends CommonResponse {

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("image")
    private String image;
    @SerializedName("experience")
    private String experience;
    @SerializedName("address")
    private String address;
    @SerializedName("subjectsList")
    private List<TutorDetailSubjects> subjectsList = new ArrayList<>();

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getExperience() {
        return experience;
    }

    public String getAddress() {
        return address;
    }

    public List<TutorDetailSubjects> getSubjectsList() {
        return subjectsList;
    }

    public static class TutorDetailSubjects {

        private String subjects = "";

        public String getSubjects() {
            return subjects;
        }
    }
}
