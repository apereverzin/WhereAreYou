package com.creek.whereareyou.android.locationlistener;

import com.creek.whereareyou.android.activity.map.WhereMapActivity;
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
        Log.d(getClass().getName(), "onLocationChanged: " + loc);
        loc.getTime();
        loc.getLatitude();
        loc.getLongitude();
        loc.getAltitude();

        GeoPoint point = new GeoPoint((int) loc.getLatitude(), (int) loc.getLongitude());
    }
}
