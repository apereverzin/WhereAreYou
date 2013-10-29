package com.creek.whereareyou.android.activity.map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.location.Location;

import com.creek.whereareyou.R;
import com.creek.whereareyou.android.colors.AnnotatedLocationImage;
import com.creek.whereareyou.android.colors.AnnotatedLocationPainter;
import com.creek.whereareyou.android.colors.Annotation;
import com.creek.whereareyou.android.colors.ColouredRectangle;
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
    private final LocationImageFactory locationImageFactory = new LocationImageFactory();
    private final AnnotatedLocationPainter painter = new AnnotatedLocationPainter();
    private final Paint paint = new Paint();
    
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
            Point locationPoint = locationImageFactory.getLocationPoint(mapView, location);
            RectF locationCircle = locationImageFactory.createLocationImage(locationPoint);
            RectF annotationBackgroundRectangle = 
                    locationImageFactory.createAnnotationBackgroundImage(locationPoint, androidContact.getDisplayName(), paint);
            Point annotationPoint = locationImageFactory.getAnnotationPoint(locationPoint, paint);
            int locationColor = ctx.getResources().getColor(R.color.location_white);
            int annotationForegroundColor = ctx.getResources().getColor(R.color.location_annotation_white);
            int annotationBackgroundColor = ctx.getResources().getColor(R.color.location_annotation_darkgrey);
            ColouredRectangle locationRectangle = new ColouredRectangle(locationCircle, locationColor);
            ColouredRectangle backgroundRectangle = new ColouredRectangle(annotationBackgroundRectangle, annotationBackgroundColor);
            Annotation annotation = 
                    new Annotation(backgroundRectangle, androidContact.getDisplayName(), annotationPoint, annotationForegroundColor);
            AnnotatedLocationImage annotatedLocationImage = new AnnotatedLocationImage(locationRectangle, annotation);
            painter.drawAnnotatedLocation(paint, canvas, annotatedLocationImage);
        }
        
        super.draw(canvas, mapView, shadow);
    }

    @Override
    public boolean onTap(GeoPoint point, MapView mapView) {
        return false;
    }
}
