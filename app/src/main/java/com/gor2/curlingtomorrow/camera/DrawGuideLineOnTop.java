package com.gor2.curlingtomorrow.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.TextureView;
import android.view.View;

public class DrawGuideLineOnTop extends View{
        TextureView textureView;
        public DrawGuideLineOnTop(Context context, TextureView textureView) {
            super(context);
            this.textureView = textureView;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            DrawBox(canvas);
            super.onDraw(canvas);
        }

        protected void DrawBox(Canvas canvas) {
            Paint paint = new Paint();
            paint.setStrokeWidth(textureView.getMeasuredWidth() / 7.0f);
            paint.setStyle(Paint.Style.STROKE);


            paint.setColor(Color.RED);
            paint.setAlpha(90);
            canvas.drawCircle(textureView.getMeasuredWidth() / 2, textureView.getMeasuredHeight() / 2, textureView.getMeasuredWidth() / 6.0f, paint);

            paint.setColor(Color.BLUE);
            paint.setAlpha(90);
            canvas.drawCircle(textureView.getMeasuredWidth() / 2, textureView.getMeasuredHeight() / 2, textureView.getMeasuredWidth() / 2.5f, paint);

        }
    }