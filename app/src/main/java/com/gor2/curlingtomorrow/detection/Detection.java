package com.gor2.curlingtomorrow.detection;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;

public class Detection {
    private RectF rect;
    private int classNumber;

    public Detection(int classNumber, RectF rect){
        this.classNumber = classNumber;
        this.rect = rect;
    }

    public int getClassNumber() {
        return classNumber;
    }

    public RectF getRect() {
        return rect;
    }

    public void setClassNumber(int classNumber) {
        this.classNumber = classNumber;
    }

    public void setRect(RectF rect) {
        this.rect = rect;
    }

    public PointF getCenter(){
        return new PointF(rect.centerX(),rect.centerY());
    }
}
