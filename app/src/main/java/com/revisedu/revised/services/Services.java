package com.revisedu.revised.services;

import com.revisedu.revised.request.CityRequest;
import com.revisedu.revised.request.CommonRequest;
import com.revisedu.revised.request.DetailRequest;
import com.revisedu.revised.request.LandmarkRequest;
import com.revisedu.revised.request.LoginRequest;
import com.revisedu.revised.request.RegisterRequest;
import com.revisedu.revised.request.SubjectRequest;
import com.revisedu.revised.response.CommonResponse;
import com.revisedu.revised.response.ListResponse;
import com.revisedu.revised.response.LoginResponse;
import com.revisedu.revised.response.ProfileResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Services {

    @POST("fetch_details.php")
    Call<CommonResponse> getDetailsResponse(@Body DetailRequest request);

    @POST("fetch_user_profile.php")
    Call<ProfileResponse> getProfileResponse(@Body CommonRequest request);

    @POST("fetch_class.php")
    Call<ListResponse> getClassServerCall();

    @POST("fetch_subject.php")
    Call<ListResponse> getSubjectServerCall(@Body SubjectRequest request);

    @POST("fetch_area.php")
    Call<ListResponse> getAreaServerCall();

    @POST("fetch_landmark.php")
    Call<ListResponse> getLandmarkServerCall(@Body LandmarkRequest request);

    @POST("fetch_city.php")
    Call<ListResponse> getCityServerCall(@Body CityRequest request);

    @POST("login.php")
    Call<LoginResponse> getLoginServerCall(@Body LoginRequest request);

    @POST("register.php")
    Call<LoginResponse> getRegisterServerCall(@Body RegisterRequest request);
}
