package com.revisedu.revised.response;

import android.text.Html;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CoachingDetailResponse {

    @SerializedName("errorCode")
    private int errorCode;
    @SerializedName("errorMessage")
    private CoachingDetail coachingDetail;

    public int getErrorCode() { return errorCode; }

    public CoachingDetail getCoachingDetail() { return coachingDetail; }

    public static class CoachingDetail {

        @SerializedName("banner")
        private String banner;
        @SerializedName("about_institute")
        private String aboutInstitute;
        @SerializedName("institute_image")
        private String instituteImage;
        @SerializedName("about_teacher")
        private String aboutTeacher;
        @SerializedName("teacher_image")
        private String teacherImage;
        @SerializedName("address")
        private String address;
        @SerializedName("subject")
        private List<CoachingSubject> subjects;
        @SerializedName("price")
        private String price;

        public String getBanner() { return banner; }

        public String getAboutInstitute() { return getData(aboutInstitute); }

        public String getInstituteImage() { return instituteImage; }

        public String getAboutTeacher() { return getData(aboutTeacher); }

        public String getTeacherImage() { return teacherImage; }

        public List<CoachingSubject> getSubjects() { return subjects; }

        public String getAddress() { return address; }

        public String getPrice() { return price; }

        private String getData(String data) {
            return Html.fromHtml(data).toString();
        }

        public static class CoachingSubject {
            @SerializedName("name")
            private String name;

            public String getName() { return name; }
        }

    }

}