package com.example.wasapp;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;


public class NotifyService extends Service {
    NotifyTask notifyTask;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notifyTask = new NotifyTask(getBaseContext());
        notifyTask.execute();
        return Service.START_STICKY_COMPATIBILITY;
    }

    @Override
    public void onDestroy() {
        notifyTask.cancel(true);
        Log.d("DEBUG: ", "Notifier service stopped...");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
