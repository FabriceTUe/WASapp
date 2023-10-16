package com.example.wasapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RetrieveTemperatureTask extends AsyncTask<Object, Void, String> {
    private String url = "http://192.168.43.82/temperature";
    private OkHttpClient httpClient = new OkHttpClient();

    @Override
    protected String doInBackground(Object[] objects) {


        Request request = new Request.Builder()
                .url(url)
                .build(); // create request to send to ESP8266

        try (Response response = httpClient.newCall(request).execute()) { // send request...
            return response.body().string(); // return contents to user
        } catch (IOException e) {
            Log.println(Log.WARN, "stderr", e.toString()); // warn developer
            return "Connection error"; // return placeholder
        }
    }
}
