package com.creek.whereareyou.android.services.inform;

import java.util.Timer;
import java.util.TimerTask;

import com.creek.whereareyou.android.locationprovider.LocationProvider;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

/**
 * 
 * @author Andrey Pereverzin
 */
public class InformService extends Service {
    private static final String TAG = InformService.class.getSimpleName();

    private Timer timer;
    protected ContentResolver contentResolver;
    
    private static final String TIMER_NAME = "WhereAreYouInformTimer";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        contentResolver = getContentResolver();

        timer = new Timer(TIMER_NAME);
        //timer.schedule(informTask, 1000L, 30 * 1000L);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer = null;
    }
    
    private TimerTask informTask = new TimerTask() {
        @Override
        public void run() {
            Log.i(TAG, "===================InformService doing work");
            Location location = new LocationProvider().getLatestLocation(InformService.this);
            //NetworkLocationProvider.onCellLocationChanged();
//            if(location != null) {
//                Log.i(TAG, "===================EmailSendingAndReceivingService doing work: " + location.getLatitude() + " " + location.getLongitude());
//                Log.i(TAG, "===================EmailSendingAndReceivingService doing work: " + System.currentTimeMillis() + " " + location.getTime());
//            }
        }
    };
}
