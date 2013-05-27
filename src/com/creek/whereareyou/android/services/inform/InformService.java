package com.creek.whereareyou.android.services.inform;

import java.util.Timer;
import java.util.TimerTask;

import com.creek.whereareyou.android.locationprovider.LocationProvider;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;

/**
 * 
 * @author andreypereverzin
 */
public class InformService extends Service {
    private static final String TAG = InformService.class.getSimpleName();

    private Timer timer;
    protected ContentResolver contentResolver;
    protected ConnectivityManager cm;
    
    private TimerTask informTask = new TimerTask() {
        @Override
        public void run() {
            Log.i(TAG, "===================EmailSendingAndReceivingService doing work");
            Location location = new LocationProvider().getLatestLocation(InformService.this);
            //NetworkLocationProvider.onCellLocationChanged();
//            if(location != null) {
//                Log.i(TAG, "===================EmailSendingAndReceivingService doing work: " + location.getLatitude() + " " + location.getLongitude());
//                Log.i(TAG, "===================EmailSendingAndReceivingService doing work: " + System.currentTimeMillis() + " " + location.getTime());
//            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        contentResolver = getContentResolver();

        timer = new Timer("WhereAreYouInformTimer");
        //timer.schedule(informTask, 1000L, 30 * 1000L);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer = null;
    }
}
