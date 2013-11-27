package com.creek.whereareyou.android.colors;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Point;
import android.graphics.RectF;
import android.location.Location;

import com.creek.whereareyou.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Projection;

/**
 * 
 * @author Andrey Pereverzin
 */
public class LocationImageFactory {

    private static final int LOCATION_RADIUS = 3;

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

    public int getLocationAccuracyInPixels(MapView mapView, float accuracy) {
        Projection projection = mapView.getProjection();
        return (int) projection.metersToEquatorPixels(accuracy);
    }

    public AnnotatedLocationImage createAnnotatedLocationImage(Context ctx, Point locationPoint, int accuracy, String[] texts, Paint paint) {
        ColouredCircledRectangle locationImage = createLocationImage(ctx, locationPoint);
        ColouredCircledRectangle backgroundRectangle = createAnnotationBackground(ctx, locationPoint, texts, paint);
        Annotation annotation = createAnnotation(ctx, locationPoint, texts, backgroundRectangle, getBackgroundRadius(paint), paint);
        return new AnnotatedLocationImage(locationPoint, accuracy, locationImage, annotation);

    }

    private Annotation createAnnotation(Context ctx, Point locationPoint, String[] texts, ColouredCircledRectangle backgroundRectangle, 
            int backgroudRadius, Paint paint) {
        int annotationForegroundColor = ctx.getResources().getColor(R.color.location_annotation_white);
        Point[] annotationPoints = getAnnotationPoints(locationPoint, backgroudRadius, texts.length, paint);
        Annotation annotation = new Annotation(backgroundRectangle, texts, annotationPoints, annotationForegroundColor);
        return annotation;
    }

    private ColouredCircledRectangle createAnnotationBackground(Context ctx, Point locationPoint, String[] texts, Paint paint) {
        int annotationBackgroundColor = ctx.getResources().getColor(R.color.location_annotation_darkgrey);
        RectF annotationBackgroundRectangle = createAnnotationBackgroundImage(locationPoint, texts, paint);
        ColouredCircledRectangle backgroundRectangle = new ColouredCircledRectangle(annotationBackgroundRectangle, annotationBackgroundColor);
        return backgroundRectangle;
    }

    private ColouredCircledRectangle createLocationImage(Context ctx, Point locationPoint) {
        int locationColor = ctx.getResources().getColor(R.color.location_blue);
        RectF locationCircle = new RectF(locationPoint.x - LOCATION_RADIUS, locationPoint.y - LOCATION_RADIUS, locationPoint.x + LOCATION_RADIUS, locationPoint.y + LOCATION_RADIUS);
        ColouredCircledRectangle locationRectangle = new ColouredCircledRectangle(locationCircle, locationColor);
        return locationRectangle;
    }

    private RectF createAnnotationBackgroundImage(Point locationPoint, String[] texts, Paint paint) {
        FontMetricsInt fm = paint.getFontMetricsInt();
        int backgroudRadius = getBackgroundRadius(paint);
        int left = locationPoint.x + 2 + backgroudRadius;
        int bottom = locationPoint.y + backgroudRadius;
        float width = measureWidth(texts, paint) + 2 * backgroudRadius;
        int height = (fm.descent - fm.ascent) * texts.length;
        return new RectF(left, bottom - height, left + width, bottom);
    }

    private Point[] getAnnotationPoints(Point locationPoint, int backgroudRadius, int lines, Paint paint) {
        FontMetricsInt fm = paint.getFontMetricsInt();
        int lineHeight = fm.descent - fm.ascent;

        Point[] points = new Point[lines];
        int x = locationPoint.x + 2 + 2 * backgroudRadius;
        int y = locationPoint.y - lineHeight * (lines - 1);
        
        for (int i = 0; i < lines; i++) {
          points[i] = new Point(x, y);
          y += lineHeight;
        }
        
        return points;
    }

    private int getBackgroundRadius(Paint paint) {
        return paint.getFontMetricsInt().descent;
    }
    
    private float measureWidth(String[] texts, Paint paint) {
        float maxWidth = 0.0f;
        
        for (int i = 0; i < texts.length; i++) {
            float width = paint.measureText(texts[i]);
            if (width > maxWidth) {
                maxWidth = width;
            }
        }
        
        return maxWidth;
    }
}
