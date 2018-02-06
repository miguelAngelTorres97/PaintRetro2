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

class Cache{
    Canvas canvas;
    Boolean used = false;
    Bitmap bp;

    Cache(Bitmap bp){
        this.bp = Bitmap.createBitmap(bp.getWidth(),bp.getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bp);
    }
}

public class PaintedView extends View {


    private Bitmap bitMap;
    private Canvas canvasBackground;
    private Cache cache;

    Paint pincel;

    public PaintedView(Context context){
        super(context);
    }

    public PaintedView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        pincel = new Paint();
        pincel.setColor(Color.WHITE);
        pincel.setAntiAlias(true);
        canvas.drawRect(0, 0, getWidth(), getHeight(), pincel);

        pincel.setStrokeWidth(10);
        canvas.drawBitmap(bitMap, 0, 0, null);
        pincel.setColor(Color.BLACK);
        pincel.setStyle(Paint.Style.STROKE);
        //canvasBackground.drawLine(xi, yi, xf, yf, pincel);
        if(painting) {
            canvas.drawLine(xi, yi, xf, yf, pincel);
            System.out.println("Painting");
        }
        //anvas.drawCircle(xi, yi, ratio, pincel);
        else
            paint();
        //canvasBackground.drawCircle(xi, yi, ratio, pincel);

    }

    private float xi, yi, xf, yf;
    private float ratio;
    private boolean painting = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                xf = xi = x;
                yf = yi = y;
                painting = true;
                break;

            case MotionEvent.ACTION_MOVE:
                //xi = xf;
                //yi = yf;
                xf = x;
                yf = y;
                break;

            case MotionEvent.ACTION_UP:
                xf = x;
                yf = y;
                painting = false;
                break;
        }
        ratio = (float) Math.sqrt(Math.pow(xi-xf,2) + Math.pow(yi-yf, 2));
        invalidate();
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        createCanvas(w, h);

    }

    private void createCanvas(int w, int h){
        bitMap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvasBackground = new Canvas(bitMap);
        cache = new Cache(bitMap);
    }

    void paint(){
        if(cache.used){
            canvasBackground.drawBitmap(cache.bp, 0, 0, null);
            cache.canvas.drawLine(xi, yi, xf, yf, pincel);
        }
        cache.used = true;
        canvasBackground.drawLine(xi, yi, xf, yf, pincel);
    }

    public void undo(){
        canvasBackground.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }

    public void chooseColor(){

    }

}

