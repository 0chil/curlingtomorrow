package com.gor2.curlingtomorrow.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.gor2.curlingtomorrow.detection.Detection;

import java.util.ArrayList;

public class DrawGuideLineOnTop extends View{
        SurfaceView surfaceView;
        public DrawGuideLineOnTop(Context context, SurfaceView surfaceView) {
            super(context);
            this.surfaceView = surfaceView;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            DrawBox(canvas);
            super.onDraw(canvas);
        }

        protected void DrawBox(Canvas canvas){
            Paint paint = new Paint();
            paint.setStrokeWidth(15);
            paint.setStyle(Paint.Style.STROKE);


        }
    }