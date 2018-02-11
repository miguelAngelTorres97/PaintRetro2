package com.example.migue.paintretro;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class PaintedView extends View {

    public ArrayList<Bitmap> cache;

    public Canvas canvas;

    public Paint pincel;

    private int mode = 0;
    private int w, h;

    public PaintedView(Context context){
        super(context);
    }

    public PaintedView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
        createCanvas();
    }

    private void createCanvas(){
        cache = new ArrayList<>();
        for(int i = 0; i<20; i++)
            cache.add(createBitmap());

        pincel = new Paint();

        pincel.setAntiAlias(true);
        pincel.setStrokeWidth(10);
        pincel.setColor(Color.BLUE);
        pincel.setStyle(Paint.Style.STROKE);

        canvas = new Canvas(cache.get(cache.size()-1));
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    Bitmap createBitmap(){
        return Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    }


    private float xi, yi, xf, yf;
    private float ratio;
    private boolean painting = false;
    private boolean resting = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                xf = xi = x;
                yf = yi = y;
                save();
                painting = true;

                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                if(mode == 0) {
                    xi = xf;
                    yi = yf;
                }
                xf = x;
                yf = y;
                break;

            case MotionEvent.ACTION_UP:
                xf = x;
                yf = y;
                painting = false;
                resting = true;
                break;
        }
        ratio = (float) Math.sqrt(Math.pow(xi-xf,2) + Math.pow(yi-yf, 2));
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas cv) {
        super.onDraw(cv);
        cv.drawColor(Color.WHITE);
        cv.drawBitmap(cache.get(cache.size() - 1), 0, 0, null);

        paint(cv);
        invalidate();
    }

    void paint(Canvas cv){

        canvas.setBitmap(cache.get(cache.size()-1));


        if(painting) {
            if(mode == 0)
                canvas.drawLine(xi, yi, xf, yf, pincel);
            if(mode == 1)
                cv.drawLine(xi, yi, xf, yf, pincel);
            if(mode == 2)
                cv.drawCircle((xi+xf)/2 , (yi+yf)/2, ratio / 2 , pincel);
                //betterCircle(cv);
        }else if(resting) {
            resting = false;
            if(mode == 1)
                canvas.drawLine(xi, yi, xf, yf, pincel);
            if(mode == 2)
                canvas.drawCircle((xi+xf)/2 , (yi+yf)/2, ratio / 2, pincel);
        }
        invalidate();
    }

    void betterCircle(Canvas cv){
        float xc, yc;
    }


    public void save(){
        for(int i = 0; i<cache.size()-1; i++){
            canvas.setBitmap(cache.get(i));
            canvas.drawBitmap(cache.get(i+1), 0 ,0, null);
        }
        canvas.setBitmap(cache.get(cache.size()-1));
    }



    public void undo(){
        cache.set(cache.size()-1, createBitmap());
        canvas.setBitmap(cache.get(cache.size()-1));
        for(int i = cache.size()-1; i>0; i--){
            cache.set(i, createBitmap());
            canvas.setBitmap(cache.get(i));
            canvas.drawBitmap(cache.get(i-1), 0 ,0, null);
        }
        canvas.setBitmap(cache.get(cache.size()-1));
        invalidate();
        mode = 2;
    }

    public void chooseColor(){
        pincel.setColor(Color.BLUE);
        mode = 1;
    }

    public void eraser(){
        pincel.setColor(Color.WHITE);
        mode = 0;
    }
}

