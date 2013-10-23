package com.creek.whereareyou.android.colors;

/**
 * 
 * @author Andrey Pereverzin
 */
public class AnnotatedLocationColors {
    private final int locationColor;
    private final int annotationForegroundColor;
    private final int annotationBackgroundColor;
 
    public AnnotatedLocationColors(int locationColor, int annotationForegroundColor, int annotationBackgroundColor) {
        this.locationColor = locationColor;
        this.annotationForegroundColor = annotationForegroundColor;
        this.annotationBackgroundColor = annotationBackgroundColor;
    }

    public int getLocationColor() {
        return locationColor;
    }

    public int getAnnotationForegroundColor() {
        return annotationForegroundColor;
    }

    public int getAnnotationBackgroundColor() {
        return annotationBackgroundColor;
    }
}
