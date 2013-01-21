package com.creek.whereareyou.android.activity.map;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.creek.whereareyou.R;
import com.creek.whereareyou.android.ApplManager;
import com.creek.whereareyou.android.overlay.LocationsOverlay;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * 
 * @author Andrey Pereverzin
 */
public class WhereMapActivity extends MapActivity {
    private static final String TAG = WhereMapActivity.class.getSimpleName();
//    private MapView mapView;
//    
//    private MapController mapController;
//    
//    @Override
//    public void onCreate(Bundle saveInstancState) {
//        super.onCreate(saveInstancState);
//
//        setContentView(R.layout.map_view);
//        mapView = (MapView)findViewById(R.id.map_view);
//        mapView.setSatellite(false);
//        mapView.setBuiltInZoomControls(true);
//        mapController = mapView.getController();
//        mapController.setZoom(17);
//
//        LocationsOverlay overlay = new LocationsOverlay();
//        mapView.getOverlays().add(overlay);
//        mapView.postInvalidate();
//
//        Location location = ApplManager.getInstance().getLocationProvider().getLocation(this);
//        
//        Double geoLat = location.getLatitude() * 1E6;
//        Double geoLng = location.getLongitude() * 1E6;
//        GeoPoint point = new GeoPoint(geoLat.intValue(), geoLng.intValue());
//        mapController.animateTo(point);
//    }
//
//    @Override
//    protected boolean isRouteDisplayed() {
//        return false;
//    }


    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    private MapController mapController;

    private LocationsOverlay locationsOverlay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Get a reference to the MapView
        MapView myMapView = (MapView) findViewById(R.id.mapView);

        // Get the Map View's controller
        mapController = myMapView.getController();

        // Configure the map display options
        myMapView.setSatellite(false);
        myMapView.setBuiltInZoomControls(true);

        // Zoom in
        mapController.setZoom(17);

        // Add the MyPositionOverlay
        locationsOverlay = new LocationsOverlay();
        List<Overlay> overlays = myMapView.getOverlays();
        overlays.add(locationsOverlay);
        myMapView.postInvalidate();

//        LocationManager locationManager;
//        String svcName = Context.LOCATION_SERVICE;
//        locationManager = (LocationManager) getSystemService(svcName);
//
//        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_FINE);
//        criteria.setPowerRequirement(Criteria.POWER_LOW);
//        criteria.setAltitudeRequired(false);
//        criteria.setBearingRequired(false);
//        criteria.setSpeedRequired(false);
//        criteria.setCostAllowed(true);
//        String provider = locationManager.getBestProvider(criteria, true);
//
//        Location l = locationManager.getLastKnownLocation(provider);
        Location l = ApplManager.getInstance().getLocationProvider().getLocation(this);

        updateWithNewLocation(l);

        //locationManager.requestLocationUpdates(provider, 2000, 10, locationListener);

    }

    private void updateWithNewLocation(Location location) {
        if (location != null) {
            // Update the position overlay.
            locationsOverlay.setLocation(location);
            Double geoLat = location.getLatitude() * 1E6;
            Double geoLng = location.getLongitude() * 1E6;
            GeoPoint point = new GeoPoint(geoLat.intValue(), geoLng.intValue());
            mapController.animateTo(point);
        }
    }

//    private final LocationListener locationListener = new LocationListener() {
//        public void onLocationChanged(Location location) {
//            updateWithNewLocation(location);
//        }
//
//        public void onProviderDisabled(String provider) {
//        }
//
//        public void onProviderEnabled(String provider) {
//        }
//
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//        }
//    };

}
