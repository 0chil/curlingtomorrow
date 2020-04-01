package com.gor2.curlingtomorrow.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.gor2.curlingtomorrow.detection.Detection;

import java.util.ArrayList;

public class DrawDetectionsOnTop extends View {
    ArrayList<Detection> detections;
    ImageView imageView;
    public DrawDetectionsOnTop(Context context, ArrayList<Detection> detections, ImageView imageView) {
        super(context);
        this.detections = detections;
        this.imageView = imageView;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        DrawBox(canvas);
        super.onDraw(canvas);
    }

    protected void DrawBox(Canvas canvas){
        Paint paint = new Paint();
        paint.setStrokeWidth(7);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(60);

        for(Detection detection : detections) {

            RectF rect = detection.getRect();
            float widthMul = imageView.getMeasuredWidth();
            float heightMul = imageView.getMeasuredWidth()*1.33f;
            rect.left *= widthMul;
            rect.right *= widthMul;
            rect.top *= heightMul;
            rect.bottom *= heightMul;

            paint.setColor(detection.getClassNumber()==0?Color.RED:Color.YELLOW);

            canvas.drawText(detection.getClassNumber()==0?"RED":"YELLOW",rect.right,rect.top,paint);
            canvas.drawRect(rect,paint);

            canvas.drawCircle(detection.getCenter().x,detection.getCenter().y,10,paint);
        }
    }
}