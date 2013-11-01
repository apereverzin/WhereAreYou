package com.creek.whereareyou.android.activity.map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Location;

import com.creek.whereareyou.android.colors.AnnotatedLocationImage;
import com.creek.whereareyou.android.colors.AnnotatedLocationPainter;
import com.creek.whereareyou.android.colors.LocationImageFactory;
import com.creek.whereareyou.android.contacts.AndroidContact;
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


    private AndroidContact androidContact;
    Location location;

    public LocationsOverlay(Context _ctx) {
        this.ctx = _ctx;
    }

    public void setContactData(AndroidContact _androidContact) {
        this.androidContact = _androidContact;
    }

    public void setLocation(Location _location) {
        this.location = _location;
    }

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {

        if (shadow == false && location != null) {
            Paint paint = new Paint();
            Point locationPoint = locationImageFactory.getLocationPoint(mapView, location);
            int accuracy = locationImageFactory.getLocationAccuracyInPixels(mapView, location.getAccuracy());
            AnnotatedLocationImage annotatedLocationImage = 
                    locationImageFactory.createAnnotatedLocationImage(ctx, locationPoint, accuracy, androidContact.getDisplayName(), paint);
            painter.drawAnnotatedLocation(canvas, annotatedLocationImage, paint);
        }
        
        super.draw(canvas, mapView, shadow);
    }

    @Override
    public boolean onTap(GeoPoint point, MapView mapView) {
        return false;
    }
}
