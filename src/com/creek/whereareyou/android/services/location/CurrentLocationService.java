package com.creek.whereareyou.android.services.location;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteRepositoryManager;
import static com.creek.whereareyou.android.infrastructure.sqlite.AbstractSQLiteRepository.UNDEFINED_INT;
import static com.creek.whereareyou.android.infrastructure.sqlite.AbstractSQLiteRepository.UNDEFINED_LONG;
import com.creek.whereareyoumodel.domain.LocationData;
import com.creek.whereareyoumodel.domain.sendable.ContactRequest;
import com.creek.whereareyoumodel.domain.sendable.ContactResponse;
import static com.creek.whereareyoumodel.domain.sendable.ResponseCode.SUCCESS;

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
            Log.i(TAG, "===================InformService doing work");
            
            List<ContactRequest> unrespondedLocationRequests = 
                    SQLiteRepositoryManager.getInstance().getContactRequestRepository().getUnrespondedLocationRequests();

            if (unrespondedLocationRequests.size() > 0) {
                LocationData locationData = 
                        SQLiteRepositoryManager.getInstance().getLocationRepository().getMyActualLocationData(locationExpirationTimeoutMs);
                LocationResponsePersistenceManager locationResponsePersistenceManager = new LocationResponsePersistenceManager();
                if (locationData == null) {
                    // Durable operation
                    locationData = locationResponsePersistenceManager.getAndPersistMyCurrentLocation(CurrentLocationService.this);
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
