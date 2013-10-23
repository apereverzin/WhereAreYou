package com.creek.whereareyou.android.colors;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 
 * @author Andrey Pereverzin
 */
public class AnnotatedLocationPainter {
    public void drawAnnotatedLocation(Canvas canvas, AnnotatedLocationImage locationImage, String annotationText, AnnotatedLocationColors colors) {
        int locationColor = colors.getLocationColor();
        int annotationForegroungColor = colors.getAnnotationForegroundColor();
        int annotationBackgroundColor = colors.getAnnotationBackgroundColor();

        Paint locationPaint = new Paint();
        locationPaint.setColor(locationColor);
        locationPaint.setAntiAlias(true);
        locationPaint.setFakeBoldText(true);

        Paint annotationForegroundPaint = new Paint();
        annotationForegroundPaint.setColor(annotationForegroungColor);
        annotationForegroundPaint.setAntiAlias(true);
        annotationForegroundPaint.setFakeBoldText(true);

        Paint annotationBackgroundPaint = new Paint();
        annotationBackgroundPaint.setColor(annotationBackgroundColor);
        annotationBackgroundPaint.setAntiAlias(true);

        canvas.drawOval(locationImage.getOval(), locationPaint);
        canvas.drawRoundRect(locationImage.getBackRect(), 5, 5, annotationBackgroundPaint);
        canvas.drawText(annotationText, locationImage.getAnnotationPoint().x, locationImage.getAnnotationPoint().y, annotationForegroundPaint);
    }
}
