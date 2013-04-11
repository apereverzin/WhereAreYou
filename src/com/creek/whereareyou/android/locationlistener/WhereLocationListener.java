package com.creek.whereareyou.android.locationlistener;

import java.util.HashSet;
import java.util.Set;

import com.creek.whereareyou.ApplManager;
import com.creek.whereareyou.android.activity.map.LocationAware;
import com.google.android.maps.GeoPoint;

import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * 
 * @author Andrey Pereverzin
 */
public class WhereLocationListener implements LocationListener {
    private static final String TAG = WhereLocationListener.class.getSimpleName();
    private final Set<LocationAware> locationAwareComponents = new HashSet<LocationAware>();

    public void onProviderEnabled(String provider) {
        Log.d(getClass().getName(), "onProviderEnabled: " + provider);
    }

    public void onProviderDisabled(String provider) {
        Log.d(getClass().getName(), "onProviderDisabled: " + provider);
    }

    /**
     * 
     */
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(getClass().getName(), "onStatusChanged: " + provider + " " + status);
        if (status == GpsStatus.GPS_EVENT_FIRST_FIX) {
            Log.d(getClass().getName(), "fix");
        } else if (status == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
            Log.d(getClass().getName(), "status");
        } else if (status == GpsStatus.GPS_EVENT_STARTED) {
            Log.d(getClass().getName(), "started");
        } else if (status == GpsStatus.GPS_EVENT_STOPPED) {
            Log.d(getClass().getName(), "stopped");
        }
    }

    /**
     * 
     */
    public void onLocationChanged(Location loc) {
//        GeoPoint point = new GeoPoint((int) loc.getLatitude(), (int) loc.getLongitude());
        System.out.println("-------------onLocationChanged " + loc.getLatitude() + " " + loc.getLongitude());
        for(LocationAware locationAwareComponent: locationAwareComponents) {
            locationAwareComponent.updateWithNewLocation(loc);
        }
        ApplManager.getInstance().getLocationProvider().setLatestLocation(loc);
    }
    
    public void addLocationAwareComponent(LocationAware component) {
        locationAwareComponents.add(component);
    }
    
    public void removeLocationAwareComponent(LocationAware component) {
        locationAwareComponents.remove(component);
    }
}
