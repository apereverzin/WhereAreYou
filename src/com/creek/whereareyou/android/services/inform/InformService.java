package com.creek.whereareyou.android.services.inform;

import java.util.Timer;
import java.util.TimerTask;

import com.creek.whereareyou.ApplManager;
import com.creek.whereareyou.android.activity.map.LocationAware;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

public class InformService extends Service implements LocationAware {
    private static final String TAG = InformService.class.getSimpleName();

    private Timer timer;
    private boolean locationUpdated = false;
    
    private TimerTask informTask = new TimerTask() {
        @Override
        public void run() {
            Log.i(TAG, "===================InformService doing work");
            ApplManager.getInstance().getLocationProvider().initiateLocationUpdates(InformService.this);
            ApplManager.getInstance().getLocationProvider().requestLocationUpdates(InformService.this);
            while(!locationUpdated) {
                try {
                    Thread.sleep(1000L);
                } catch(InterruptedException ex) {
                    //
                }
                Log.i(TAG, "===================+++++++InformService doing work");
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        timer = new Timer("WhereAreYouInformTimer");
        timer.schedule(informTask, 1000L, 20 * 1000L);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer = null;
    }

    @Override
    public void updateWithNewLocation(Location loc) {
        Log.i(TAG, "-------------------InformService doing work " + loc.getLatitude() + " " + loc.getLongitude());
        locationUpdated = true;
    }
}
