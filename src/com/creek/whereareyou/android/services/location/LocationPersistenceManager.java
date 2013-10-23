package com.creek.whereareyou.android.services.location;

import static com.creek.whereareyou.android.infrastructure.sqlite.AbstractSQLiteRepository.UNDEFINED_INT;
import static com.creek.whereareyou.android.infrastructure.sqlite.AbstractSQLiteRepository.UNDEFINED_LONG;
import static com.creek.whereareyoumodel.domain.sendable.ResponseCode.SUCCESS;

import java.util.List;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.creek.whereareyou.android.db.ContactResponseEntity;
import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteRepositoryManager;
import com.creek.whereareyou.android.locationprovider.LocationProvider;
import com.creek.whereareyoumodel.domain.ContactCompoundId;
import com.creek.whereareyoumodel.domain.LocationData;
import com.creek.whereareyoumodel.domain.sendable.ContactRequest;
import com.creek.whereareyoumodel.domain.sendable.ContactResponse;
import com.creek.whereareyoumodel.repository.ContactRequestRepository;
import com.creek.whereareyoumodel.repository.ContactResponseRepository;
import com.creek.whereareyoumodel.repository.LocationRepository;

/**
 * 
 * @author Andrey Pereverzin
 */
public class LocationPersistenceManager {
    private static final String TAG = LocationPersistenceManager.class.getSimpleName();

    List<ContactRequest> getUnrespondedContactLocationRequests() {
        Log.d(TAG, "getUnrespondedContactLocationRequests()");
        try {
            SQLiteRepositoryManager.getInstance().openDatabase();
            ContactRequestRepository contactRequestRepository = SQLiteRepositoryManager.getInstance().getContactRequestRepository();
            
            List<ContactRequest> unrespondedLocationRequests = contactRequestRepository.getIncomingUnrespondedLocationRequests();
            Log.d(TAG, "--------------LocationRequestManager: " + unrespondedLocationRequests.size());
            
            return unrespondedLocationRequests;
        } finally {
            SQLiteRepositoryManager.getInstance().closeDatabase();
        }
    }

    LocationData getMyActualLocationData(int locationExpirationTimeoutMs) {
        Log.d(TAG, "getMyActualLocationData()");
        try {
            SQLiteRepositoryManager.getInstance().openDatabase();
            LocationRepository locationRepository = SQLiteRepositoryManager.getInstance().getLocationRepository();
            
            return locationRepository.getMyActualLocationData(locationExpirationTimeoutMs);
        } finally {
            SQLiteRepositoryManager.getInstance().closeDatabase();
        }
    }

    LocationData getAndPersistMyCurrentLocation(Context ctx) {
        Log.d(TAG, "getAndPersistMyCurrentLocation()");
        Location location = new LocationProvider().getLatestLocation(ctx);
        LocationData locationData = new LocationData();
        locationData.setContactCompoundId(new ContactCompoundId(null, null));
        locationData.setLocationTime(location.getTime());
        locationData.setAccuracy(location.getAccuracy());
        locationData.setLatitude(location.getLatitude());
        locationData.setLongitude(location.getLongitude());
        locationData.setSpeed(location.getSpeed());
        locationData.setHasAccuracy(location.hasAccuracy());
        locationData.setHasSpeed(location.hasSpeed());
        
        try {
            SQLiteRepositoryManager.getInstance().openDatabase();
            LocationRepository locationRepository = SQLiteRepositoryManager.getInstance().getLocationRepository();

            Log.d(TAG, "--------------getAndPersistMyCurrentLocation: " + locationData);
            locationData = (LocationData) locationRepository.create(locationData);

            return locationData;
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            SQLiteRepositoryManager.getInstance().closeDatabase();
        }
    }
    
    void persistLocationResponses(List<ContactRequest> unrespondedLocationRequests, LocationData locationData) {
        Log.d(TAG, "persistLocationResponses()");
        Log.d(TAG, "--------------persistLocationResponses()");
        try {
            SQLiteRepositoryManager.getInstance().openDatabase();
            ContactResponseRepository<ContactResponseEntity> contactResponseRepository = SQLiteRepositoryManager.getInstance().getContactResponseRepository();
            ContactRequestRepository contactRequestRepository = SQLiteRepositoryManager.getInstance().getContactRequestRepository();
            
            // TODO should create one location response per user, not per request
            // TODO mark all user's requests as 'processed' in one go
            for (int i = 0; i < unrespondedLocationRequests.size(); i++) {
                ContactRequest request = unrespondedLocationRequests.get(i);
                persistLocationResponse(contactResponseRepository, request, locationData);
                markLocationRequestAsProcessed(contactRequestRepository, request);
            }
        } finally {
            SQLiteRepositoryManager.getInstance().closeDatabase();
        }
    }
    
    private ContactResponse persistLocationResponse(ContactResponseRepository<ContactResponseEntity> contactResponseRepository, 
            ContactRequest request, LocationData locationData) {
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
        contactResponseRepository.create(response);
        Log.d(TAG, "--------------persistLocationResponse: " + response);
        return response;
    }
    
    private ContactRequest markLocationRequestAsProcessed(ContactRequestRepository contactRequestRepository, ContactRequest request) {
        Log.d(TAG, "markLocationRequestAsProcessed()");
        request.setProcessed(true);
        contactRequestRepository.update(request);
        Log.d(TAG, "--------------markLocationRequestAsProcessed: " + request);
        return request;
    }
}
