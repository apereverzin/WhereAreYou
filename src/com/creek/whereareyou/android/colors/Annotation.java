package com.creek.whereareyou.android.colors;

import android.graphics.Point;

/**
 * 
 * @author Andrey Pereverzin
 */
public class Annotation {
    private final ColouredCircledRectangle backgroundRectangle;
    private final String[] texts;
    private final Point[] points;
    private final int foregroundColour;

    public Annotation(ColouredCircledRectangle _backgroundRectangle, String[] _texts, Point[] _points, int _foregroundColour) {
        this.backgroundRectangle = _backgroundRectangle;
        this.texts = _texts;
        this.points = _points;
        this.foregroundColour = _foregroundColour;
    }

    public ColouredCircledRectangle getBackgroundRectangle() {
        return backgroundRectangle;
    }

    public String[] getTexts() {
        return texts;
    }

    public Point[] getPoints() {
        return points;
    }

    public int getForegroundColour() {
        return foregroundColour;
    }
}
