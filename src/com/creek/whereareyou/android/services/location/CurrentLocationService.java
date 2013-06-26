package com.creek.whereareyou.android.services.location;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteRepositoryManager;
import com.creek.whereareyou.android.locationprovider.LocationProvider;
import com.creek.whereareyoumodel.domain.sendable.ContactRequest;

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
public class CurrentLocationService extends Service {
    private static final String TAG = CurrentLocationService.class.getSimpleName();

    private Timer timer;
    protected ContentResolver contentResolver;
    
    private TimerTask informTask = new TimerTask() {
        @Override
        public void run() {
            Log.i(TAG, "===================InformService doing work");
            
            // TODO
            List<ContactRequest> unsentRequests = SQLiteRepositoryManager.getInstance().getContactRequestRepository().getUnrespondedLocationRequests();

            Location location = new LocationProvider().getLatestLocation(CurrentLocationService.this);
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
        contentResolver = getContentResolver();

        timer = new Timer("WhereAreYouInformTimer");
        timer.schedule(informTask, 1000L, 30 * 1000L);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer = null;
    }
}
