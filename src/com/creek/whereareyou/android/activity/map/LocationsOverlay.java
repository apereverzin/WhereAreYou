package com.creek.whereareyou.android.activity.map;

//import android.R;
import android.content.Context;
import android.graphics.Canvas;
import android.location.Location;

import com.creek.whereareyou.R;
import com.creek.whereareyou.android.colors.AnnotatedLocationColors;
import com.creek.whereareyou.android.colors.AnnotatedLocationImage;
import com.creek.whereareyou.android.colors.AnnotatedLocationPainter;
import com.creek.whereareyou.android.colors.LocationImageFactory;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * 
 * @author Andrey Pereverzin
 */
public class LocationsOverlay extends Overlay {
    private LocationImageFactory locationImageFactory = new LocationImageFactory();
    private AnnotatedLocationPainter painter = new AnnotatedLocationPainter();
    
    private final Context ctx;

    private static final int RADIUS = 3;

    Location location;

    public LocationsOverlay(Context _ctx) {
        this.ctx = _ctx;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {

        if (shadow == false && location != null) {
            AnnotatedLocationImage locationImage = locationImageFactory.createAnnotatedLocationImage(mapView, location, RADIUS);
            int locationColor = ctx.getResources().getColor(R.color.location_white);
            int annotationForegroundColor = ctx.getResources().getColor(R.color.location_annotation_white);
            int annotationBackgroundColor = ctx.getResources().getColor(R.color.location_annotation_darkgrey);
            AnnotatedLocationColors colors = new AnnotatedLocationColors(locationColor, annotationForegroundColor, annotationBackgroundColor);
            painter.drawAnnotatedLocation(canvas, locationImage, "Here I Am", colors);
        }
        
        super.draw(canvas, mapView, shadow);
    }

    @Override
    public boolean onTap(GeoPoint point, MapView mapView) {
        return false;
    }
}
