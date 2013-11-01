package com.creek.whereareyou.android.colors;

import android.graphics.Point;

/**
 * 
 * @author Andrey Pereverzin
 */
public class AnnotatedLocationImage {
    private final Point locationPoint;
    private final int accuracy;
    private final ColouredCircledRectangle locationImage;
    private final Annotation annotation;
    
    public AnnotatedLocationImage(Point _locationPoint, int _accuracy, ColouredCircledRectangle _locationRectangle, Annotation _annotation) {
        this.locationPoint = _locationPoint;
        this.accuracy = _accuracy;
        this.locationImage = _locationRectangle;
        this.annotation = _annotation;
    }
    
    public Point getLocationPoint() {
        return locationPoint;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public ColouredCircledRectangle getLocationImage() {
        return locationImage;
    }
    
    public Annotation getAnnotation() {
        return annotation;
    }
}
