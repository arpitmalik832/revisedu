package com.revisedu.revised.retrofit;

import com.revisedu.revised.services.Services;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.internal.Util;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class RetrofitApi {

    private RetrofitApi() {
        /*
         * This is done just to satisfy the sonar
         * */
    }

    private static final String BASE_URL = "https://revisedu.com/dashboard/api/";

    private static Services sServices = null;

    public static Services getServicesObject() {
        if (null == sServices) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.level(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).callTimeout(25, TimeUnit.SECONDS).protocols(Util.immutableListOf(Protocol.HTTP_1_1)).build();
            Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
            sServices = retrofit.create(Services.class);
        }
        return sServices;
    }
}
