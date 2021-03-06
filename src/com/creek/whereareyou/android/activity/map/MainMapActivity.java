package com.creek.whereareyou.android.activity.map;

import static android.view.Menu.FIRST;
import static com.creek.whereareyou.android.util.ActivityUtil.logException;
import static com.creek.whereareyou.android.util.DataConversionUtil.getLocationFromLocationResponse;
import static com.creek.whereareyou.android.util.Util.formatLocationTime;

import java.io.IOException;
import java.util.List;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.creek.whereareyou.R;
import com.creek.whereareyou.android.activity.account.EmailAccountAddress_1_Activity;
import com.creek.whereareyou.android.activity.contacts.ContactsActivity;
import com.creek.whereareyou.android.contacts.AndroidContact;
import com.creek.whereareyou.android.contacts.ContactsPersistenceManager;
import com.creek.whereareyou.android.locationprovider.LocationProvider;
import com.creek.whereareyoumodel.domain.LocationData;
import com.creek.whereareyoumodel.message.OwnerLocationDataMessage;

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
    private static final int EMAIL_ACCOUNT_MENU_ITEM = FIRST;
    private static final int VIEW_CONTACTS_MENU_ITEM = FIRST + 1;


    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    private MapController mapController;

    private LocationsOverlay locationsOverlay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        
        setContentView(R.layout.map);
        MapView mapView = (MapView) findViewById(R.id.mapView);
        mapController = mapView.getController();
        mapView.setSatellite(false);
        mapView.setBuiltInZoomControls(true);
        mapController.setZoom(17);

        locationsOverlay = new LocationsOverlay(this);
        List<Overlay> overlays = mapView.getOverlays();
        overlays.add(locationsOverlay);
        mapView.postInvalidate();
        
        //locationProvider = new LocationProvider();

        //locationProvider.initiateLocationUpdates(this);
        //locationProvider.requestLocationUpdates(this);

        Bundle extras = getIntent().getExtras();
        Log.d(TAG, "++++++++++++++++++++1");
        if (extras != null) {
            @SuppressWarnings("unchecked")
            List<OwnerLocationDataMessage> locationResponses = (List<OwnerLocationDataMessage>)extras.get(RECEIVED_LOCATIONS);
            Log.d(TAG, "++++++++++++++++++++2: " + locationResponses.size());
            for (int i = 0; i < locationResponses.size(); i++) {
                OwnerLocationDataMessage locationResponse = locationResponses.get(i);
                Log.d(TAG, "++++++++++++++++++++: " + locationResponse);
            }
            if (locationResponses.size() > 0) {
                try {
                    OwnerLocationDataMessage locationResponse = locationResponses.get(0);
                    String contactEmail = locationResponse.getSenderEmail();
                    AndroidContact androidContact = ContactsPersistenceManager.getInstance().getAndroidContactByEmail(this, contactEmail);
                    LocationData locationData = locationResponse.getOwnerLocationData().getLocationData();
                    Location location = getLocationFromLocationResponse(locationData);

                    updateWithNewContactDataAndLocation(androidContact, locationData.getLocationTime(), location);
                } catch (IOException ex) {
                    logException(TAG, ex);
                }
            }
        }
        Log.d(TAG, "++++++++++++++++++++3");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu() called");

        menu.add(0, EMAIL_ACCOUNT_MENU_ITEM, 0, R.string.menu_edit_email_account);
        menu.add(0, VIEW_CONTACTS_MENU_ITEM, 0, R.string.menu_view_contacts);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case EMAIL_ACCOUNT_MENU_ITEM:
            Intent intent = new Intent(MainMapActivity.this, EmailAccountAddress_1_Activity.class);
            startActivity(intent);
            return true;
        case VIEW_CONTACTS_MENU_ITEM:
            startContactsActivity();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onDestroy() {
        if (locationProvider != null) {
            locationProvider.stopLocationUpdates(this);
        }
        super.onDestroy();
    }

    @Override
    public void updateWithNewLocation(long locationTime, Location location) {
        Log.d(TAG, "updateWithNewLocation()");
        Log.d(TAG, "------------updateWithNewLocation()");
        if (location != null) {
            Log.d(TAG, "updateWithNewLocation(): " + location.getLatitude() + ", " + location.getLongitude());
            Log.d(TAG, "------------updateWithNewLocation(): " + location.getLatitude() + ", " + location.getLongitude());
            locationsOverlay.setContactData(null);
            locationsOverlay.setLocation(location);
            locationsOverlay.setLocationTime(formatLocationTime(locationTime));
            Double geoLat = location.getLatitude() * 1E6;
            Double geoLng = location.getLongitude() * 1E6;
            GeoPoint point = new GeoPoint(geoLat.intValue(), geoLng.intValue());
            mapController.animateTo(point);
        }
    }

    @Override
    public void updateWithNewContactDataAndLocation(AndroidContact androidContact, long locationTime, Location location) {
        Log.d(TAG, "updateWithNewLocation()");
        Log.d(TAG, "------------updateWithNewLocation()");
        if (location != null) {
            Log.d(TAG, "updateWithNewLocation(): " + location.getLatitude() + ", " + location.getLongitude());
            Log.d(TAG, "------------updateWithNewLocation(): " + location.getLatitude() + ", " + location.getLongitude());
            locationsOverlay.setContactData(androidContact);
            locationsOverlay.setLocation(location);
            locationsOverlay.setLocationTime(formatLocationTime(locationTime));
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
