/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine.data.network;

import android.net.Uri;
import android.util.Log;

import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * These utilities will be used to communicate with the weather servers.
 */
final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    /*
     * Sunshine was originally built to use OpenWeatherMap's API. However, we wanted to provide
     * a way to much more easily test the app and provide more varied weather data. After all, in
     * Mountain View (Google's HQ), it gets very boring looking at a forecast of perfectly clear
     * skies at 75°F every day... (UGH!) The solution we came up with was to host our own fake
     * weather server. With this server, there are two URL's you can use. The first (and default)
     * URL will return dynamic weather data. Each time the app refreshes, you will get different,
     * completely random weather data. This is incredibly useful for testing the robustness of your
     * application, as different weather JSON will provide edge cases for some of your methods.
     *
     */
    private static final String DYNAMIC_WEATHER_URL =
            "https://andfun-weather.udacity.com/weather/";

    private static final String STATIC_WEATHER_URL =
            "https://andfun-weather.udacity.com/staticweather";

    private static final String FORECAST_BASE_URL = DYNAMIC_WEATHER_URL;

    /*
     * NOTE: These values only effect responses from OpenWeatherMap, NOT from the fake weather
     * server. They are simply here to allow us to teach you how to build a URL if you were to use
     * a real API. If you want to connect your app to OpenWeatherMap's API, feel free to! However,
     * we are not going to show you how to do so in this training.
     */

    /* The format we want our API to return */
    private static final String format = "json";
    /* The units we want our API to return */
    private static final String units = "metric";


    /* The query parameter allows us to provide a location string to the API */
    private static final String QUERY_PARAM = "q";

    /* The format parameter allows us to designate whether we want JSON or XML from our API */
    private static final String FORMAT_PARAM = "mode";
    /* The units parameter allows us to designate whether we want metric units or imperial units */
    private static final String UNITS_PARAM = "units";
    /* The days parameter allows us to designate how many days of weather data we want */
    private static final String DAYS_PARAM = "cnt";
    /* Retrofit client instance for caching purposes */
    private static Retrofit retrofit = null;
    /*OKHttpclient for logging purposes*/
    private static OkHttpClient okHttpClient = null;

    /**
     * Retrieves the proper URL to query for the weather data.
     *
     * @return URL to query weather service
     */
    static URL getUrl() {
        String locationQuery = "Mountain View, CA";
        return buildUrlWithLocationQuery(locationQuery);
    }

    public static OkHttpClient getOkHttpClient() {

        if (okHttpClient == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        }
        return okHttpClient;
    }


    public static Retrofit getRetrofitClient() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(FORECAST_BASE_URL)
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                            .registerTypeAdapter(WeatherResponse.class, new WeatherResponseDeserializer())
                            .create()))
                    .build();
        }
        return retrofit;
    }

    /**
     * Builds the URL used to talk to the weather server using a location. This location is based
     * on the query capabilities of the weather provider that we are using.
     *
     * @param locationQuery The location that will be queried for.
     * @return The URL to use to query the weather server.
     */
    private static URL buildUrlWithLocationQuery(String locationQuery) {
        Uri weatherQueryUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                                 .appendQueryParameter(QUERY_PARAM, locationQuery)
                                 .appendQueryParameter(FORMAT_PARAM, format)
                                 .appendQueryParameter(UNITS_PARAM, units)
                                 .appendQueryParameter(DAYS_PARAM, Integer.toString(WeatherNetworkDataSource.NUM_DAYS))
                                 .build();

        try {
            URL weatherQueryUrl = new URL(weatherQueryUri.toString());
            Log.v(TAG, "URL: " + weatherQueryUrl);
            return weatherQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }


    static WeatherResponse getResponseFromRetrofit(URL url) throws IOException {
        ApiInterface service = getRetrofitClient().create(ApiInterface.class);
        return service.getResponse(url.toString()).execute().body();

    }


}