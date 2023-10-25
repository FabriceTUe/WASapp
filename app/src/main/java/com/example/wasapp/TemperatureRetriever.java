package com.example.wasapp;

import java.io.IOException;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TemperatureRetriever {
    private static OkHttpClient httpClient = new OkHttpClient();

    public static TempStruct getTemperatures(Settings settings) throws
            IOException, NullPointerException {
            TempStruct tempStruct = new TempStruct();
            tempStruct.interiorTemperature = retrieveTemperature(settings.getInteriorUrl());
            tempStruct.exteriorTemperature = retrieveTemperature(settings.getExteriorUrl());

            return tempStruct;
    }

    private static float retrieveTemperature(String url) throws
            IOException, NullPointerException {
        Request request = new Request.Builder()
                .url(url)
                .build(); // create request to send to ESP8266
        Response response = httpClient.newCall(request).execute(); // send request
        String body = response.peekBody(2048).string();
        if (body == null) {
            throw new NullPointerException();
        }
        return Float.parseFloat(response.body().string());
    }
}
