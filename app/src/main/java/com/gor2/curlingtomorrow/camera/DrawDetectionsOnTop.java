package com.gor2.curlingtomorrow.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.widget.FrameLayout;

import com.gor2.curlingtomorrow.Curlingtomorrow;
import com.gor2.curlingtomorrow.dataclass.Detection;

import java.util.ArrayList;

public class DrawDetectionsOnTop extends View {
    final ArrayList<Detection> detections;
    FrameLayout frameLayout;

    float widthMul;
    float heightMul;
    public DrawDetectionsOnTop(Context context, final ArrayList<Detection> detections, FrameLayout frameLayout) {
        super(context);
        this.detections = detections;
        this.frameLayout = frameLayout;
        widthMul = frameLayout.getMeasuredWidth();
        heightMul = frameLayout.getMeasuredWidth()*1.33f;
        for(Detection detection : detections){
            detection.getRect().left *= widthMul;
            detection.getRect().right *= widthMul;
            detection.getRect().top *= heightMul;
            detection.getRect().bottom *= heightMul;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        DrawBox(canvas);
    }

    protected void DrawBox(Canvas canvas){
        Paint paint = new Paint();
        paint.setStrokeWidth(7);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(60);

        for(Detection detection : detections) {
            RectF rect = detection.getRect();

            paint.setColor(detection.getClassNumber()==Curlingtomorrow.redStoneClassNumber?Color.RED:Color.YELLOW);

            //canvas.drawText(detection.getClassNumber()==Curlingtomorrow.redStoneClassNumber?"RED":"YELLOW",rect.right,rect.top,paint);
            canvas.drawRect(rect,paint);

            canvas.drawCircle(detection.getCenter().x,detection.getCenter().y,10,paint);
        }
    }
}