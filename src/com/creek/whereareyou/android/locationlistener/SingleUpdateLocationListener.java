package com.creek.whereareyou.android.locationlistener;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

/**
 * 
 * @author andreypereverzin
 */
public class SingleUpdateLocationListener extends AbstractLocationListener implements LocationListener {
    private static final String TAG = SingleUpdateLocationListener.class.getSimpleName();

    private final LocationManager locationManager;
    
    public SingleUpdateLocationListener(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    public void onLocationChanged(Location location) {
        System.out.println("---------------------onLocationChanged " + location.getProvider() + " " + location.getLatitude() + " " + location.getLongitude());
        locationManager.removeUpdates(this);
    }
}
