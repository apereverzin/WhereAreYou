package com.creek.whereareyou.android.colors;

import android.graphics.RectF;

/**
 * 
 * @author Andrey Pereverzin
 */
public class ColouredCircledRectangle {
    private final RectF rectangle;
    private final int color;

    public ColouredCircledRectangle(RectF rectangle, int color) {
        this.rectangle = rectangle;
        this.color = color;
    }
    public RectF getRectangle() {
        return rectangle;
    }
    public int getColor() {
        return color;
    }

}
