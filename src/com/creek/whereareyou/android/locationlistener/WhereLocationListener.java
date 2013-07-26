package com.creek.whereareyou.android.locationlistener;

import java.util.HashSet;
import java.util.Set;

import com.creek.whereareyou.android.activity.map.LocationAware;

import static android.location.GpsStatus.GPS_EVENT_FIRST_FIX;
import static android.location.GpsStatus.GPS_EVENT_SATELLITE_STATUS;
import static android.location.GpsStatus.GPS_EVENT_STARTED;
import static android.location.GpsStatus.GPS_EVENT_STOPPED;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * 
 * @author Andrey Pereverzin
 */
public class WhereLocationListener extends AbstractLocationListener implements LocationListener {
    private static final String TAG = WhereLocationListener.class.getSimpleName();
    
    private final Set<LocationAware> locationAwareComponents = new HashSet<LocationAware>();

    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, "onStatusChanged: " + provider + " " + status);
        if (status == GPS_EVENT_FIRST_FIX) {
            Log.d(TAG, "fix");
        } else if (status == GPS_EVENT_SATELLITE_STATUS) {
            Log.d(TAG, "status");
        } else if (status == GPS_EVENT_STARTED) {
            Log.d(TAG, "started");
        } else if (status == GPS_EVENT_STOPPED) {
            Log.d(TAG, "stopped");
        }
    }

    /**
     * 
     */
    public void onLocationChanged(Location loc) {
//        GeoPoint point = new GeoPoint((int) loc.getLatitude(), (int) loc.getLongitude());
        Log.d(TAG, "onLocationChanged: " + loc.getProvider() + " " + loc.getLatitude() + " " + loc.getLongitude());
        Log.d(TAG, "-------------onLocationChanged: " + loc.getProvider() + " " + loc.getLatitude() + " " + loc.getLongitude());
        for (LocationAware locationAwareComponent: locationAwareComponents) {
            locationAwareComponent.updateWithNewLocation(loc);
        }
    }
    
    public void addLocationAwareComponent(LocationAware component) {
        locationAwareComponents.add(component);
    }
    
    public void removeLocationAwareComponent(LocationAware component) {
        locationAwareComponents.remove(component);
    }
}
