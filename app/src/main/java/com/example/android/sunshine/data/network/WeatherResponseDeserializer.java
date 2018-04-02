package com.example.android.sunshine.data.network;


import com.example.android.sunshine.data.database.WeatherEntry;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

public class WeatherResponseDeserializer implements JsonDeserializer<WeatherResponse> {


    @Override
    public WeatherResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        try {
            // Is there an error?
            if (OpenWeatherJsonParser.hasHttpError(json.getAsJsonObject())) {
                return null;
            }

            JSONObject forecastJson = new JSONObject(json.getAsJsonObject().toString());
            WeatherEntry[] weatherForecast = OpenWeatherJsonParser.fromJson(forecastJson);
            return new WeatherResponse(weatherForecast);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }
}
