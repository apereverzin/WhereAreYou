package com.creek.whereareyou.android.colors;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

/**
 * 
 * @author Andrey Pereverzin
 */
public class AnnotatedLocationPainter {
    public void drawAnnotatedLocation(Paint paint, Canvas canvas, AnnotatedLocationImage annotatedLocationImage) {

        Annotation annotation = annotatedLocationImage.getAnnotation();
        
        int locationColor = annotatedLocationImage.getLocationRectangle().getColor();
        int annotationForegroungColor = annotation.getForegroundColour();
        int annotationBackgroundColor = annotation.getBackgroundRectangle().getColor();
        
        drawLocation(canvas, annotatedLocationImage.getLocationRectangle().getRectangle(), locationColor, paint);

        drawAnnotationBackground(canvas, annotation, annotationBackgroundColor, paint);

        drawAnnotation(canvas, annotation, annotationForegroungColor, paint);
    }
    
    public void drawLocation(Canvas canvas, ColouredRectangle locationImage) {
        drawLocation(canvas, locationImage.getRectangle(), locationImage.getColor(), new Paint());
    }

    private void drawAnnotation(Canvas canvas, Annotation annotation, int annotationForegroungColor, Paint paint) {
        Point annotationPoint = annotation.getPoint();
        paint.setColor(annotationForegroungColor);
        paint.setAntiAlias(true);
        paint.setFakeBoldText(true);
        canvas.drawText(annotation.getText(), annotationPoint.x, annotationPoint.y, paint);
    }

    private void drawAnnotationBackground(Canvas canvas, Annotation annotation, int annotationBackgroundColor, Paint paint) {
        paint.setColor(annotationBackgroundColor);
        paint.setAntiAlias(true);
        paint.setFakeBoldText(false);
        canvas.drawRoundRect(annotation.getBackgroundRectangle().getRectangle(), 5, 5, paint);
    }

    private void drawLocation(Canvas canvas, RectF rect, int locationColor, Paint paint) {
        paint.setColor(locationColor);
        paint.setAntiAlias(true);
        paint.setFakeBoldText(true);
        canvas.drawOval(rect, paint);
    }
}
