package com.gor2.curlingtomorrow.dataclass;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Detection {
    private RectF rect;
    private int classNumber;
    private Float distance;

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

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
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

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Detection(classNumber,new RectF(rect));
    }
}
