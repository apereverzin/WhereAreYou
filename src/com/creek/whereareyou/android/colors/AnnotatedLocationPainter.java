package com.creek.whereareyou.android.colors;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

/**
 * 
 * @author Andrey Pereverzin
 */
public class AnnotatedLocationPainter {
    public void drawAnnotatedLocation(Canvas canvas, AnnotatedLocationImage annotatedLocationImage) {

        Annotation annotation = annotatedLocationImage.getAnnotation();
        
        int locationColor = annotatedLocationImage.getLocationRectangle().getColor();
        int annotationForegroungColor = annotation.getForegroundColour();
        int annotationBackgroundColor = annotation.getBackgroundRectangle().getColor();

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

        canvas.drawOval(annotatedLocationImage.getLocationRectangle().getRectangle(), locationPaint);
        canvas.drawRoundRect(annotation.getBackgroundRectangle().getRectangle(), 5, 5, annotationBackgroundPaint);
        Point annotationPoint = annotation.getPoint();
        canvas.drawText(annotation.getText(), annotationPoint.x, annotationPoint.y, annotationForegroundPaint);
    }
    
    public void drawLocation(Canvas canvas, ColouredRectangle locationImage) {
        int locationColor = locationImage.getColor();

        Paint locationPaint = new Paint();
        locationPaint.setColor(locationColor);
        locationPaint.setAntiAlias(true);
        locationPaint.setFakeBoldText(true);

        canvas.drawOval(locationImage.getRectangle(), locationPaint);
    }
}
