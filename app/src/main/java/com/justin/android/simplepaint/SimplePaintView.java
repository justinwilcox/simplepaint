package com.justin.android.simplepaint;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.util.LruCache;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class SimplePaintView extends View {

    //our second owned canvas object that we'll actually draw paths too
    private Canvas spCanvas;

    //the bitmap that stores the painting
    private Bitmap spBitmap;

    //painting config
    private Paint spPaint;

    //the current line the user is drawing
    private Path currentPath;

    public int paintColor;

    //our backing paths are drawn into a bitmap, we can cache this bitmap so we don't lose it during any configuration changes
    private static LruCache<String, Bitmap> bitmapCache = new LruCache<String, Bitmap>(1);


    public SimplePaintView(Context context) {
        super(context);
    }

    public SimplePaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SimplePaintView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SimplePaintView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public void setPaintColor(int color) {

        this.spPaint = new Paint();

        this.spPaint.setColor(color);

        this.spPaint.setStyle(Paint.Style.STROKE);
        this.spPaint.setStrokeWidth(8);
    }

    public int getPaintColor() {
        return spPaint.getColor();
    }

    public void setPaintCanvasSize(int width, int height) {

        //note to make handling orientation change easier, we're just going to make the underlying
        // bitmap square so we don't have to worry about resizing it during orientation change

        int size = width;
        if(height > size) size = height;

        spBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        spCanvas = new Canvas(this.spBitmap);
    }

    public void clearPainting() {
        bitmapCache.remove("painting");

        //easiest way to clear all the painting is to just create a new backing bitmap
        //a new bitmap is created whenever the size is set
        if(spBitmap != null) {
            setPaintCanvasSize(spBitmap.getWidth(), spBitmap.getHeight());
        }

        invalidate();
    }

    public void cachePainting() {
        if(spBitmap != null) {
            bitmapCache.put("painting", spBitmap);
        }
    }

    public void restoreCachedPainting() {
        Bitmap cached = bitmapCache.get("painting");

        if(cached != null) {
            spBitmap = cached;
            spCanvas = new Canvas(spBitmap);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //just draw our bitmap, which has all the paths painted to it
        canvas.drawBitmap(spBitmap, 0, 0, spPaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //Log.i("SimplePaintCanvas", "Touch Event: " + event);

        if(event.getPointerCount() >= 2) {
            //we handle the touch, but don't actually do anything if there are more than one touch points
            return true;
        }

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.currentPath = new Path();
                currentPath.moveTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                this.currentPath.lineTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                this.currentPath.lineTo(event.getX(), event.getY());
                break;
            default:
                break;
        }


        this.drawCurrentPath();
        this.invalidate();

        return true;
    }


    private void drawCurrentPath() {

        this.spCanvas.drawPath(this.currentPath, this.spPaint);
    }


}



