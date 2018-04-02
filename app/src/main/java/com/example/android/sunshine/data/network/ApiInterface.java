package com.example.android.sunshine.data.network;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;


public interface ApiInterface {

    @GET
    Call<WeatherResponse> getResponse(@Url String url);

}

