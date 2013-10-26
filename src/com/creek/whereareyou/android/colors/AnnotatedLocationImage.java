package com.creek.whereareyou.android.colors;

/**
 * 
 * @author Andrey Pereverzin
 */
public class AnnotatedLocationImage {
    private final ColouredRectangle locationRectangle;
    private final Annotation annotation;
    
    public AnnotatedLocationImage(ColouredRectangle _locationRectangle, Annotation _annotation) {
        this.locationRectangle = _locationRectangle;
        this.annotation = _annotation;
    }
    
    public ColouredRectangle getLocationRectangle() {
        return locationRectangle;
    }
    
    public Annotation getAnnotation() {
        return annotation;
    }
}
