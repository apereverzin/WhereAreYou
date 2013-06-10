package com.creek.whereareyou.android.locationlistener;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * 
 * @author Andrey Pereverzin
 */
public abstract class AbstractLocationListener implements LocationListener {
    private static final String TAG = AbstractLocationListener.class.getSimpleName();
    
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, "onStatusChanged");
    }
    
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "onProviderEnabled");
    }
    
    public void onProviderDisabled(String provider) {
        Log.d(TAG, "onProviderDisabled");
    }
    
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged");
    }
}
