package com.quduquxie.communal.crop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.quduquxie.Constants;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class BitmapUtil {

    public static Bitmap decodeUriAsBitmap(Context context, Uri uri) {
        if (context == null || uri == null) return null;

        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    public static boolean saveBitmapToFile(Bitmap bitmap, String filename) {
        Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(Constants.APP_PATH_AVATAR + filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap.compress(compressFormat, quality, outputStream);
    }
}