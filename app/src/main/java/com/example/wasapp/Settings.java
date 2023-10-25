package com.example.wasapp;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Settings {
    private float targetTemperature;
    private float temperatureTolerance;
    private int messageDelay;
    private String exteriorUrl;
    private String interiorUrl;
    private static final String fileName = "settings.json";

    // Singleton
    private static Settings instance;
    private Settings() {}
    public static synchronized Settings getInstance(Context context) {
        if (instance == null) {
            Settings loadedInstance = tryLoad(context); // try loading settings...
            if (loadedInstance == null) {
                instance = new Settings();
                instance.targetTemperature = 24; // set defaults...
                instance.temperatureTolerance = 0;
                instance.messageDelay = 30;
                instance.exteriorUrl = "http://192.168.43.82/temperature";
                instance.interiorUrl = "http://192.168.43.234/temperature";
            } else {
                instance = loadedInstance;
            }
        }
        return instance;
    }

    public void save(Context context) {
        try {
            Gson gson = new Gson();
            String settingsJson = gson.toJson(instance); // convert singleton instance to json
            writeToFile(settingsJson, context);
        } catch (JsonIOException e) {
            Log.println(Log.WARN, "stderr", e.toString());
        } catch (IOException e) {
            Log.println(Log.WARN, "stderr", e.toString());
        }
    }

    private void writeToFile(String data, Context context)
            throws IOException {
        OutputStreamWriter outputStreamWriter =
                new OutputStreamWriter(context.openFileOutput(fileName,
                        Context.MODE_PRIVATE));
        outputStreamWriter.write(data);
        outputStreamWriter.close();
    }

    private static Settings tryLoad(Context context) {
        try {
            File file = new File(context.getFilesDir(), fileName);
            String settingsJson = readFile(file);
            Gson gson = new Gson();
            return gson.fromJson(settingsJson, Settings.class);
        } catch (FileNotFoundException e) {
            Log.println(Log.INFO, "stdout","file not found: +\n" + e);
            return null;
        } catch (JsonIOException | IOException e) {
            Log.println(Log.WARN, "stderr", e.toString());
            return null;
        }
    }

    private static String readFile(File file) throws IOException {
        // read file...
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        isr.close();
        fis.close();
        return sb.toString();
    }

    public void setTargetTemperature(float targetTemperature) { this.targetTemperature = targetTemperature; }
    public float getTargetTemperature() {
        return targetTemperature;
    }

    public void setTemperatureTolerance(float temperatureTolerance) { this.temperatureTolerance = temperatureTolerance; }
    public float getTemperatureTolerance() {
        return temperatureTolerance;
    }

    public void setMessageDelay(int messageDelay) {
        this.messageDelay = messageDelay;
    }
    public int getMessageDelay() {
        return messageDelay;
    }

    public void setExteriorUrl(String exteriorUrl) { this.exteriorUrl = exteriorUrl; }
    public String getExteriorUrl() { return exteriorUrl; }

    public void setInteriorUrl(String interiorUrl) { this.interiorUrl = interiorUrl; }
    public String getInteriorUrl() { return interiorUrl; }
}
