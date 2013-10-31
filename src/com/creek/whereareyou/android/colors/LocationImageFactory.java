package com.creek.whereareyou.android.colors;

import android.graphics.Point;
import android.graphics.RectF;
import android.location.Location;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Projection;

/**
 * 
 * @author Andrey Pereverzin
 */
public class LocationImageFactory {

    public Point getLocationPoint(MapView mapView, Location location) {
        Projection projection = mapView.getProjection();
        Double latitude = location.getLatitude() * 1E6;
        Double longitude = location.getLongitude() * 1E6;
        GeoPoint geoPoint;
        geoPoint = new GeoPoint(latitude.intValue(), longitude.intValue());

        // Convert the location to screen pixels
        Point point = new Point();
        projection.toPixels(geoPoint, point);
        
        return point;
    }
    
    public RectF createLocationImage(Point locationPoint, int radius) {
        return new RectF(locationPoint.x - radius, locationPoint.y - radius, locationPoint.x + radius, locationPoint.y + radius);
    }
    
    public RectF createAnnotationBackgroundImage(Point locationPoint, int radius) {
        return new RectF(locationPoint.x + 2 + radius, locationPoint.y - 3 * radius, locationPoint.x + 65, locationPoint.y + radius);
    }
    
    public Point getAnnotationPoint(Point locationPoint, int radius) {
        return new Point(locationPoint.x + 2 * radius, locationPoint.y);
    }
}
