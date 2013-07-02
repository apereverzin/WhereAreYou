package com.creek.whereareyou.android.services.location;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteRepositoryManager;
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
    private int locationExpirationTimeoutInMinutes = 5;
    private int locationExpirationTimeoutMs = locationExpirationTimeoutInMinutes * MILLISECONDS_IN_MINUTE;

    private TimerTask informTask = new TimerTask() {
        @Override
        public void run() {
            Log.i(TAG, "===================CurrentLocationService doing work");
            
            List<ContactRequest> unrespondedLocationRequests = 
                    SQLiteRepositoryManager.getInstance().getContactRequestRepository().getUnrespondedLocationRequests();
            System.out.println("--------------CurrentLocationService: " + unrespondedLocationRequests.size());

            if (unrespondedLocationRequests.size() > 0) {
                LocationResponsePersistenceManager locationResponsePersistenceManager = new LocationResponsePersistenceManager();

                LocationData locationData = 
                        SQLiteRepositoryManager.getInstance().getLocationRepository().getMyActualLocationData(locationExpirationTimeoutMs);
                System.out.println("--------------CurrentLocationService: " + locationData);
                if (locationData == null) {
                    // Durable operation
                    System.out.println("--------------CurrentLocationService locationData==null");
                    locationData = locationResponsePersistenceManager.getAndPersistMyCurrentLocation(CurrentLocationService.this);
                    System.out.println("--------------CurrentLocationService: " + locationData);
                }
                
                for (int i = 0; i < unrespondedLocationRequests.size(); i++) {
                    ContactRequest request = unrespondedLocationRequests.get(i);
                    locationResponsePersistenceManager.persistLocationResponse(request, locationData);
                }
            }

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
