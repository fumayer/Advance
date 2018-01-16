package com.example.sj.app2.seekbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawTextStudy extends SurfaceView implements SurfaceHolder.Callback {

    SurfaceHolder holder;
    public DrawTextStudy(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        holder = this.getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        new Thread(new MyThread()).start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }

    void drawText(Canvas canvas , String text , float x , float y, Paint paint , float angle){
        if(angle != 0){
            canvas.rotate(angle, x, y);
        }
        canvas.drawText(text, x, y, paint);
        if(angle != 0){
            canvas.rotate(-angle, x, y);
        }
    }

    class MyThread implements Runnable{

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Canvas canvas = null;
            try{
                canvas = holder.lockCanvas();
                Paint paint = new Paint();
                paint.setColor(Color.WHITE);
                paint.setTextSize(20);
                canvas.drawLine(100, 100, 100, 400, paint);
                drawText(canvas,"Hello", 80, 200, paint,-90);
                canvas.drawLine(100, 100, 200, 400, paint);

                paint.setColor(Color.RED);
                paint.setTextSize(40);
                drawText(canvas,"free", 150, 180, paint,-45);

                paint.setColor(Color.BLUE);
                drawText(canvas,"World", 150, 80, paint,0);
                canvas.drawLine(100, 100, 400, 100, paint);

            } catch(Exception e){

            } finally {
                holder.unlockCanvasAndPost(canvas);
            }

        }
    }


}