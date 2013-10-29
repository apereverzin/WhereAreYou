package com.creek.whereareyou.android.colors;

import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
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
    private static final int RADIUS = 3;

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
    
    public RectF createLocationImage(Point locationPoint) {
        return new RectF(locationPoint.x - RADIUS, locationPoint.y - RADIUS, locationPoint.x + RADIUS, locationPoint.y + RADIUS);
    }
    
    public RectF createAnnotationBackgroundImage(Point locationPoint, String text, Paint paint) {
        float f = paint.measureText(text);
        int desc = paint.getFontMetricsInt().descent;
        int height = paint.getFontMetricsInt().descent + paint.getFontMetricsInt().ascent;
        int annotationRectX = calcAnnotationRectX(locationPoint.x, desc);
        int annotationRectY = locationPoint.y - 3 * desc;
        
        return new RectF(annotationRectX, annotationRectY, annotationRectX + f + desc + 2, annotationRectY + height);
    }
    
    public Point getAnnotationPoint(Point locationPoint, Paint paint) {
        int desc = paint.getFontMetricsInt().descent;
        return new Point(calcAnnotationRectX(locationPoint.x, desc) + desc, locationPoint.y);
    }
    
    private int calcAnnotationRectX(int locationX, int desc) {
        return locationX + 2 + desc;
    }
}
