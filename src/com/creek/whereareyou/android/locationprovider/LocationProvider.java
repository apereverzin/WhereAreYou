package com.creek.whereareyou.android.locationprovider;

import java.util.List;

import com.creek.whereareyou.android.activity.map.LocationAware;
import com.creek.whereareyou.android.locationlistener.SingleUpdateLocationListener;
import com.creek.whereareyou.android.locationlistener.WhereLocationListener;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * 
 * @author Andrey Pereverzin
 */
public class  LocationProvider {
    private static final String TAG = LocationProvider.class.getSimpleName();

    private String bestProvider;
    private LocationManager locationManager;
    private WhereLocationListener locationListener = new WhereLocationListener();
        
    public void initiateLocationUpdates(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        bestProvider = defineBestProvider(context);

        Log.d(TAG, "----------------------------==========================bestProvider: " + bestProvider);
        locationManager.requestLocationUpdates(bestProvider, 2000, 10, locationListener);
    }
    
    public Location getLatestLocation(final Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        String bestProvider = defineBestProvider(context);
        System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-==-=-=-=-=-bestProvider: " + bestProvider);
        Location loc = locationManager.getLastKnownLocation(bestProvider);
        if (loc != null) {
            System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-==-=-=-=-=-: " + bestProvider + " " + loc.getAccuracy() + " " + loc.getTime() + " " + loc.getLatitude() + " " + loc.getLongitude());
        } else {
            System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-==-=-=-=-=- loc null");
        }
        
        Location bestLocation = null;
        float bestAccuracy = Float.MAX_VALUE;
        long bestTime = 0L;
        long minTime = System.nanoTime() - 60000L;
        List<String> allProviders = locationManager.getAllProviders();

        Looper l = Looper.getMainLooper();

        for (String provider: allProviders) {
            boolean c = locationManager.sendExtraCommand(provider, "START", null);
            System.out.println("+++++++++++++++: " + provider + " enabled: " + locationManager.isProviderEnabled(provider) + " " + c);
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                System.out.println("+++++++++++++++-: " + bestAccuracy + " " + bestTime + " " + minTime);
                System.out.println("+++++++++++++++: " + provider + " " + location.getAccuracy() + " " + location.getTime() + " " + location.getLatitude() + " " + location.getLongitude());
                float accuracy = location.getAccuracy();
                long time = location.getTime();
                if (time >= minTime && accuracy < bestAccuracy) {
                    System.out.println("+++++++++++++++: 1");
                    bestLocation = location;
                    bestTime = time;
                    bestAccuracy = accuracy;
                } else if (time < minTime && bestAccuracy == Float.MAX_VALUE && time > bestTime) {
                    System.out.println("+++++++++++++++: 2");
                    bestLocation = location;
                    bestTime = time;
                }
            }
            LocationListener ll = new SingleUpdateLocationListener(locationManager);
            locationManager.requestLocationUpdates(provider, 1000L, 1f, ll, l);
        }
        return bestLocation;
    }
    
    public void requestLocationUpdates(LocationAware listener) {
        locationListener.addLocationAwareComponent(listener);
    }
    
    public void stopLocationUpdates(LocationAware listener) {
        locationListener.removeLocationAwareComponent(listener);
    }
    
    private String defineBestProvider(Context context) {
        Criteria criteria = createCriteria();
        
        String prov = locationManager.getBestProvider(criteria, true);
        
        return prov;
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
}
