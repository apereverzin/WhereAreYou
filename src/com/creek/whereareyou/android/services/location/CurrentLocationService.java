package com.creek.whereareyou.android.services.location;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.creek.whereareyoumodel.domain.LocationData;
import com.creek.whereareyoumodel.domain.sendable.ContactRequest;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
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
    
    private static final int MILLISECONDS_IN_MINUTE = 60 * 1000;
    // TODO make configurable
    private int locationExpirationTimeoutInMinutes = 2;
    private int locationExpirationTimeoutMs = locationExpirationTimeoutInMinutes * MILLISECONDS_IN_MINUTE;
    
    private static final String TIMER_NAME = "WhereAreYouCurrentLocationTimer";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        contentResolver = getContentResolver();

        timer = new Timer(TIMER_NAME);
        timer.schedule(currentLocationTask, 1000L, locationExpirationTimeoutMs);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer = null;
    }

    private TimerTask currentLocationTask = new TimerTask() {
        @Override
        public void run() {
            Log.i(TAG, "===================CurrentLocationService doing work");
            LocationPersistenceManager locationPersistenceManager = new LocationPersistenceManager();
            
            List<ContactRequest> unrespondedLocationRequests = locationPersistenceManager.getUnrespondedContactLocationRequests();
            Log.d(TAG, "--------------unrespondedLocationRequests: " + unrespondedLocationRequests.size());
            
            if (unrespondedLocationRequests.size() > 0) {
                LocationData locationData = locationPersistenceManager.getMyActualLocationData(locationExpirationTimeoutMs);
                Log.d(TAG, "--------------CurrentLocationService: " + locationData);
                if (locationData == null) {
                    // Durable operation
                    Log.d(TAG, "--------------CurrentLocationService locationData==null");
                    locationData = locationPersistenceManager.getAndPersistMyCurrentLocation(CurrentLocationService.this);
                    Log.d(TAG, "--------------CurrentLocationService: " + locationData);
                }

                locationPersistenceManager.persistLocationResponses(unrespondedLocationRequests, locationData);
            }

            //NetworkLocationProvider.onCellLocationChanged();
//            if(location != null) {
//                Log.i(TAG, "===================EmailSendingAndReceivingService doing work: " + location.getLatitude() + " " + location.getLongitude());
//                Log.i(TAG, "===================EmailSendingAndReceivingService doing work: " + System.currentTimeMillis() + " " + location.getTime());
//            }
        }
    };
}
