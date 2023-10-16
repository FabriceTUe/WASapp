package com.example.wasapp;

import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TemperatureRetriever {
    private static String url = "http://192.168.43.82/temperature";
    private static OkHttpClient httpClient = new OkHttpClient();

    public static float retrieveTemperature() throws
            IOException, NullPointerException, InterruptedException {
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
