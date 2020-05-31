package com.revisedu.revised.services;

import com.revisedu.revised.request.DetailRequest;
import com.revisedu.revised.request.LandmarkRequest;
import com.revisedu.revised.response.DetailResponse;
import com.revisedu.revised.response.ListResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Services {

    @POST("fetch_details.php")
    Call<DetailResponse> getDetailsResponse(@Body DetailRequest request);

    @POST("fetch_area.php")
    Call<ListResponse> getAreaServerCall();

    @POST("fetch_landmark.php")
    Call<ListResponse> getLandmarkServerCall(@Body LandmarkRequest request);
}
