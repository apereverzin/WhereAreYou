package com.creek.whereareyou.android.locationprovider;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

/**
 * 
 * @author Andrey Pereverzin
 */
public class  LocationProvider {
    private static final String TAG = LocationProvider.class.getSimpleName();

    private String bestProvider;
    private boolean bestProviderDefined = false;
    private LocationManager locationManager;
    
    public Location getLocation(Context context) {
        if(!bestProviderDefined) {
            defineBestProvider(context);
        }
        
        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        Log.d(TAG, "----- " + bestProvider);
        Location l = locationManager.getLastKnownLocation(bestProvider);
        
        return l;
    }
    
    private void defineBestProvider(Context context) {
        Criteria criteria = createCriteria();
        
        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        bestProvider = locationManager.getBestProvider(criteria, true);
        
        bestProviderDefined = true;
    }
    
    private Criteria createCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        return criteria;
    }
}
