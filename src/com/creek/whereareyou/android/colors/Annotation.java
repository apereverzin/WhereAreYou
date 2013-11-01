package com.creek.whereareyou.android.colors;

import android.graphics.Point;

/**
 * 
 * @author Andrey Pereverzin
 */
public class Annotation {
    private final ColouredCircledRectangle backgroundRectangle;
    private final String text;
    private final Point point;
    private final int foregroundColour;

    public Annotation(ColouredCircledRectangle backgroundRectangle, String text, Point point, int foregroundColour) {
        this.backgroundRectangle = backgroundRectangle;
        this.text = text;
        this.point = point;
        this.foregroundColour = foregroundColour;
    }

    public ColouredCircledRectangle getBackgroundRectangle() {
        return backgroundRectangle;
    }

    public String getText() {
        return text;
    }

    public Point getPoint() {
        return point;
    }

    public int getForegroundColour() {
        return foregroundColour;
    }
}
