package com.creek.whereareyou.android.services.locationservice;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;

/**
 * 
 * @author Andrey Pereverzin
 */
public class LocationService extends Service {
    private static final String TAG = LocationService.class.getSimpleName();

    private Timer timer;
    LocationManager locationManager;
    String bestProvider;
    
    private TimerTask updateTask = new TimerTask() {
        @Override
        public void run() {
            Log.i(TAG, "-------------------Timer task doing work");
            Location l = locationManager.getLastKnownLocation(bestProvider);
            Log.i(TAG, "-------------------" + l.getLatitude() + " " + l.getLongitude());
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        timer = new Timer("StaccatoTimer");
        timer.schedule(updateTask, 1000L, 1 * 1000L);
        
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);
        
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        bestProvider = locationManager.getBestProvider(criteria, true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer = null;
    }
}
