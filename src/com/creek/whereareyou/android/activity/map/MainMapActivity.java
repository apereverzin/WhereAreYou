package com.creek.whereareyou.android.activity.map;

import java.util.List;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.creek.whereareyou.R;
import com.creek.whereareyou.android.activity.account.EmailAccountEditActivity;
import com.creek.whereareyou.android.activity.contacts.ContactsActivity;
import com.creek.whereareyou.android.locationprovider.LocationProvider;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * 
 * @author Andrey Pereverzin
 */
public class MainMapActivity extends MapActivity implements LocationAware {
    private static final String TAG = MainMapActivity.class.getSimpleName();

    private LocationProvider locationProvider;

    public static final String RECEIVED_LOCATIONS = "received_locations";
    private static final int EMAIL_ACCOUNT_MENU_ITEM = Menu.FIRST;
    private static final int CONTACTS_TO_TRACE_MENU_ITEM = Menu.FIRST + 1;
    private static final int CONTACTS_TO_INFORM_MENU_ITEM = Menu.FIRST + 2;

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    private MapController mapController;

    private LocationsOverlay locationsOverlay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            Log.i( "dd","Extra:" + extras.getString("item_id") );
        }
        
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
        
        locationProvider = new LocationProvider();

        locationProvider.initiateLocationUpdates(this);
        locationProvider.requestLocationUpdates(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu() called");

        menu.add(0, EMAIL_ACCOUNT_MENU_ITEM, 0, R.string.menu_edit_email_account);
        menu.add(0, CONTACTS_TO_INFORM_MENU_ITEM, 0, R.string.menu_contacts_to_inform);
        menu.add(0, CONTACTS_TO_TRACE_MENU_ITEM, 0, R.string.menu_contacts_to_trace);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case EMAIL_ACCOUNT_MENU_ITEM:
            Intent intent = new Intent(MainMapActivity.this, EmailAccountEditActivity.class);
            startActivity(intent);
            return true;
        case CONTACTS_TO_TRACE_MENU_ITEM:
            startContactsActivity();
            return true;
        case CONTACTS_TO_INFORM_MENU_ITEM:
            startContactsActivity();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onDestroy() {
        locationProvider.stopLocationUpdates(this);
        super.onDestroy();
    }

    @Override
    public void updateWithNewLocation(Location location) {
        if (location != null) {
            locationsOverlay.setLocation(location);
            Double geoLat = location.getLatitude() * 1E6;
            Double geoLng = location.getLongitude() * 1E6;
            GeoPoint point = new GeoPoint(geoLat.intValue(), geoLng.intValue());
            mapController.animateTo(point);
        }
    }

    private void startContactsActivity() {
        Intent intent = new Intent(MainMapActivity.this, ContactsActivity.class);
        startActivity(intent);
    }
}
