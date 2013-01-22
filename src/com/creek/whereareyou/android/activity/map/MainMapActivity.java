package com.creek.whereareyou.android.activity.map;

import java.util.List;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.creek.whereareyou.R;
import com.creek.whereareyou.android.ApplManager;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * 
 * @author Andrey Pereverzin
 */
public class MainMapActivity extends MapActivity {
    private static final String TAG = MainMapActivity.class.getSimpleName();

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    private MapController mapController;

    private LocationsOverlay locationsOverlay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.map);
        MapView mapView = (MapView) findViewById(R.id.mapView);
        mapController = mapView.getController();
        mapView.setSatellite(false);
        mapView.setBuiltInZoomControls(true);
        mapController.setZoom(17);

        locationsOverlay = new LocationsOverlay();
        List<Overlay> overlays = mapView.getOverlays();
        overlays.add(locationsOverlay);
        mapView.postInvalidate();

        Location l = ApplManager.getInstance().getLocationProvider().getLocation(this, locationListener);

        updateWithNewLocation(l);
    }

    private void updateWithNewLocation(Location location) {
        if (location != null) {
            locationsOverlay.setLocation(location);
            Double geoLat = location.getLatitude() * 1E6;
            Double geoLng = location.getLongitude() * 1E6;
            GeoPoint point = new GeoPoint(geoLat.intValue(), geoLng.intValue());
            mapController.animateTo(point);
        }
    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            updateWithNewLocation(location);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

}
