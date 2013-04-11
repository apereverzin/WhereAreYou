package com.creek.whereareyou.android.locationprovider;

import java.util.HashSet;
import java.util.Set;

import com.creek.whereareyou.android.activity.map.LocationAware;
import com.creek.whereareyou.android.locationlistener.WhereLocationListener;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

/**
 * 
 * @author Andrey Pereverzin
 */
public class  LocationProvider {
    private static final String TAG = LocationProvider.class.getSimpleName();

    private String bestProvider;
    private boolean locationUpdatesRequested = false;
    private LocationManager locationManager;
    private Location latestLocation;
    private WhereLocationListener locationListener = new WhereLocationListener();
        
    public void initiateLocationUpdates(Context context) {
        if (!locationUpdatesRequested) {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            defineBestProvider(context);

            Log.d(TAG, "----------------------------==========================bestProvider: " + bestProvider);
            locationManager.requestLocationUpdates(bestProvider, 2000, 10, locationListener);
            
            locationUpdatesRequested = true;
        }
    }
    
    public void requestLocationUpdates(LocationAware listener) {
        locationListener.addLocationAwareComponent(listener);
    }
    
    public void stopLocationUpdates(LocationAware listener) {
        locationListener.removeLocationAwareComponent(listener);
    }
    
    private void defineBestProvider(Context context) {
        Criteria criteria = createCriteria();
        
        bestProvider = locationManager.getBestProvider(criteria, true);
    }
    
    private Criteria createCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);
        return criteria;
    }

    public Location getLatestLocation(Context context) {
        if (!locationUpdatesRequested) {
            initiateLocationUpdates(context);
        }
        
        return latestLocation;
    }

    public void setLatestLocation(Location latestLocation) {
        this.latestLocation = latestLocation;
    }
}
