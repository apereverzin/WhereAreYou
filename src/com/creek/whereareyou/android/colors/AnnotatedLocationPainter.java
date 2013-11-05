package com.creek.whereareyou.android.colors;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

/**
 * 
 * @author Andrey Pereverzin
 */
public class AnnotatedLocationPainter {
    public void drawAnnotatedLocation(Canvas canvas, AnnotatedLocationImage annotatedLocationImage, Paint paint) {

        Annotation annotation = annotatedLocationImage.getAnnotation();
        
        drawLocation(canvas, annotatedLocationImage, paint);

        drawAnnotationBackground(canvas, annotation, paint);

        drawAnnotationText(canvas, annotation, paint);
    }
    
    public void drawLocation(Canvas canvas, ColouredCircledRectangle locationImage) {
        int locationColor = locationImage.getColor();

        Paint locationPaint = new Paint();
        locationPaint.setColor(locationColor);
        locationPaint.setAntiAlias(true);
        locationPaint.setFakeBoldText(true);

        canvas.drawOval(locationImage.getRectangle(), locationPaint);
    }

    private void drawAnnotationText(Canvas canvas, Annotation annotation, Paint paint) {
        paint.setColor(annotation.getForegroundColour());
        paint.setAntiAlias(true);
        paint.setFakeBoldText(true);
        Point annotationPoint = annotation.getPoint();
        canvas.drawText(annotation.getText(), annotationPoint.x, annotationPoint.y, paint);
    }

    private void drawAnnotationBackground(Canvas canvas, Annotation annotation, Paint paint) {
        paint.setColor(annotation.getBackgroundRectangle().getColor());
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(annotation.getBackgroundRectangle().getRectangle(), 5, 5, paint);
    }

    private void drawLocation(Canvas canvas, AnnotatedLocationImage annotatedLocationImage, Paint paint) {
        drawLocationAccuracyCircle(canvas, annotatedLocationImage, paint);

        paint.setColor(annotatedLocationImage.getLocationImage().getColor());
        paint.setAntiAlias(true);
        paint.setFakeBoldText(true);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawOval(annotatedLocationImage.getLocationImage().getRectangle(), paint);
    }
    
    private void drawLocationAccuracyCircle(Canvas canvas, AnnotatedLocationImage annotatedLocationImage, Paint paint) {
        paint.setColor(annotatedLocationImage.getLocationImage().getColor());
        paint.setAntiAlias(true);
        paint.setFakeBoldText(true);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(annotatedLocationImage.getLocationPoint().x, annotatedLocationImage.getLocationPoint().y, 
                annotatedLocationImage.getAccuracy(), paint);
    }
}
