package com.creek.whereareyou.android.colors;

import android.graphics.RectF;

/**
 * 
 * @author Andrey Pereverzin
 */
public class ColouredRectangle {
    private final RectF rectangle;
    private final int color;

    public ColouredRectangle(RectF rectangle, int color) {
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
