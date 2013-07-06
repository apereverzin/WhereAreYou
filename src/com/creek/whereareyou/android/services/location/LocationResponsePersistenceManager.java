package com.creek.whereareyou.android.services.location;

import static com.creek.whereareyou.android.infrastructure.sqlite.AbstractSQLiteRepository.UNDEFINED_INT;
import static com.creek.whereareyou.android.infrastructure.sqlite.AbstractSQLiteRepository.UNDEFINED_LONG;
import static com.creek.whereareyoumodel.domain.sendable.ResponseCode.SUCCESS;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.creek.whereareyou.android.db.ContactResponseEntity;
import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteRepositoryManager;
import com.creek.whereareyou.android.locationprovider.LocationProvider;
import com.creek.whereareyoumodel.domain.LocationData;
import com.creek.whereareyoumodel.domain.sendable.ContactRequest;
import com.creek.whereareyoumodel.domain.sendable.ContactResponse;

/**
 * 
 * @author Andrey Pereverzin
 */
public class LocationResponsePersistenceManager {
    private static final String TAG = LocationResponsePersistenceManager.class.getSimpleName();

    LocationData getAndPersistMyCurrentLocation(Context context) {
        Log.d(TAG, "getAndPersistMyCurrentLocation()");
        Log.d(TAG, "--------------getAndPersistMyCurrentLocation");
        Location location = new LocationProvider().getLatestLocation(context);
        LocationData locationData = new LocationData();
        locationData.setLocationTime(location.getTime());
        locationData.setAccuracy(location.getAccuracy());
        locationData.setLatitude(location.getLatitude());
        locationData.setLongitude(location.getLongitude());
        locationData.setSpeed(location.getSpeed());
        locationData.setHasAccuracy(location.hasAccuracy());
        locationData.setHasSpeed(location.hasSpeed());
        
        Log.d(TAG, "--------------getAndPersistMyCurrentLocation: " + locationData);
        SQLiteRepositoryManager.getInstance().getLocationRepository().create(locationData);

        return locationData;
    }
    
    ContactResponse persistLocationResponse(ContactRequest request, LocationData locationData) {
        Log.d(TAG, "persistLocationResponse()");
        ContactResponseEntity response = new ContactResponseEntity();
        response.setContactCompoundId(request.getContactCompoundId());
        response.setTimeCreated(System.currentTimeMillis());
        response.setTimeSent(UNDEFINED_LONG);
        response.setTimeReceived(UNDEFINED_LONG);
        response.setResultCode(UNDEFINED_INT);
        response.setProcessed(false);
        response.setResponseCode(SUCCESS);
        response.setMessage("");
        response.setRequest(request);
        response.setRequestId(request.getId());
        response.setLocationData(locationData);
        response.setLocationDataId(locationData.getId());
        SQLiteRepositoryManager.getInstance().getContactResponseRepository().create(response);
        Log.d(TAG, "--------------persistLocationResponse: " + response);
        return response;
    }
}
