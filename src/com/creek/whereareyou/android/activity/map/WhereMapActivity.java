package com.creek.whereareyou.android.activity.map;

import android.os.Bundle;

import com.creek.whereareyou.R;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class WhereMapActivity extends MapActivity {
    private MapView mapView;
    
    private MapController mapController;
    
    @Override
    public void onCreate(Bundle saveInstancState) {
        super.onCreate(saveInstancState);
        setContentView(R.layout.map_view);
        mapView = (MapView)findViewById(R.id.map_view);
        mapController = mapView.getController();
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}
