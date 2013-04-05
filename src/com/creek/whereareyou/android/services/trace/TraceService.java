package com.creek.whereareyou.android.services.trace;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;

import com.creek.whereareyou.android.services.location.LocationService;

public class TraceService extends Service {
    private static final String TAG = LocationService.class.getSimpleName();

    private Timer timer;
    LocationManager locationManager;
    String bestProvider;
    
    private TimerTask traceTask = new TimerTask() {
        @Override
        public void run() {
            Log.i(TAG, "-------------------Timer task doing work");
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        timer = new Timer("WhereAreYouTraceTimer");
        timer.schedule(traceTask, 1000L, 1 * 1000L);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer = null;
    }
}
