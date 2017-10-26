package com.quduquxie.read.animation;

import android.graphics.Bitmap;

import com.quduquxie.Constants;
import com.quduquxie.application.QuApplication;
import com.quduquxie.base.config.BaseConfig;

import java.util.ArrayList;

public class BitmapManager {

    private static final int SIZE = 5;
    private final Bitmap[] myBitmaps = new Bitmap[SIZE];

    private int myWidth;
    private int myHeight;
    private int mFootHeight;

    public static final int CURRENT = 0;
    public static final int NEXT = 1;
    private int spaceHeight;

    public BitmapManager(int width, int height) {
//        if (QuApplication.getDisplayMetrics() != null) {
//            spaceHeight = (int) (QuApplication.getDisplayMetrics().density * 10);
//        } else {
            spaceHeight = 20;
//        }

        this.myWidth = width;
        this.myHeight = height;
        if (BaseConfig.READING_FLIP_UP_DOWN) {
            this.myHeight += spaceHeight;
        }
    }

    ArrayList<Bitmap> bitmaps = new ArrayList<>();

    public Bitmap getBitmap() {
        Bitmap bitmap;
        try {
            bitmap = Bitmap.createBitmap(myWidth, myHeight, Bitmap.Config.RGB_565);
        } catch (OutOfMemoryError e) {
            System.gc();
            System.runFinalization();
            bitmap = Bitmap.createBitmap(myWidth, myHeight, Bitmap.Config.RGB_565);
        }
        bitmaps.add(bitmap);
        return bitmap;
    }

    public Bitmap getBitmap(int which) {
        if (which >= SIZE) {
            throw new IllegalArgumentException();
        }
        if (which < 3) {
            if (myBitmaps[which] == null) {
                try {
                    myBitmaps[which] = Bitmap.createBitmap(myWidth, myHeight, Bitmap.Config.RGB_565);
                } catch (OutOfMemoryError e) {
                    System.gc();
                    System.gc();
                    System.gc();
                    System.runFinalization();
                    try {
                        myBitmaps[which] = Bitmap.createBitmap(myWidth, myHeight, Bitmap.Config.RGB_565);
                    } catch (OutOfMemoryError e2) {
                        System.gc();
                        System.gc();
                        System.gc();
                        System.runFinalization();
                        myBitmaps[which] = Bitmap.createBitmap(myWidth, myHeight, Bitmap.Config.RGB_565);
                    }

                }

            }
        } else {
            if (myBitmaps[which] == null) {
                try {
                    myBitmaps[which] = Bitmap.createBitmap(myWidth, mFootHeight, Bitmap.Config.RGB_565);
                } catch (OutOfMemoryError e) {
                    System.gc();
                    System.gc();
                    System.gc();
                    System.runFinalization();
                    try {
                        myBitmaps[which] = Bitmap.createBitmap(myWidth, mFootHeight, Bitmap.Config.RGB_565);
                    } catch (OutOfMemoryError e2) {
                        System.gc();
                        System.gc();
                        System.gc();
                        System.runFinalization();
                        myBitmaps[which] = Bitmap.createBitmap(myWidth, mFootHeight, Bitmap.Config.RGB_565);
                    }

                }

            }
        }


        return myBitmaps[which];
    }

    public void clearBitmap() {
        for (int i = 0; i < myBitmaps.length; i++) {
            if (myBitmaps[i] != null && !myBitmaps[i].isRecycled()) {
                myBitmaps[i].recycle();
                myBitmaps[i] = null;
            }
        }
        for (int i = 0; i < bitmaps.size(); i++) {
            Bitmap bitmap = bitmaps.get(i);
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
        }
        bitmaps.removeAll(bitmaps);
        bitmaps = null;

        System.gc();
    }

}
