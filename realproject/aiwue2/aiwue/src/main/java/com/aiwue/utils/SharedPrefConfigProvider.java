package com.aiwue.utils;

import android.content.ContentProvider;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import com.aiwue.base.AiwueApplication;
import com.orhanobut.logger.Logger;

/**
 * Created by liaixiong on 16-8-25.
 */

public class SharedPrefConfigProvider extends ContentProvider {

    public static final Uri CONFIG_CONTENT_URI		= Uri.parse("content://com.aiwue.config.provider");

    private static final int LENGTH_CONTENT_URI		= CONFIG_CONTENT_URI.toString().length() + 1;
    @Override
    public boolean onCreate() {
        Logger.d("SharedPrefConfigProvider created successfully !!!");
        ProcessCheck.SetServiceProcess();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        return null;
    }

    private static String EXTRA_TYPE		= "type";
    private static String EXTRA_KEY			= "key";
    private static String EXTRA_VALUE		= "value";

    private static final int TYPE_BOOLEAN	= 1;
    private static final int TYPE_INT		= 2;
    private static final int TYPE_LONG		= 3;
    private static final int TYPE_STRING	= 4;
    private static final int TYPE_FLOAT     = 5;


    private static ContentResolver getContentResolver(){
        return AiwueApplication.getAppContext().getContentResolver();
    }

    private static boolean s_bFixedSysBug = false;
    private static Object  s_LockFixedBug = new Object();
    private static ContentProviderClient s_cpClientFixer = null;

    private static void FixProviderSystemBug(){
        synchronized (s_LockFixedBug) {
            if ( s_bFixedSysBug )
                return;
            s_bFixedSysBug = true;

            if ((Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT <= 18)
                    || (Build.VERSION.SDK_INT >= 9 && Build.VERSION.SDK_INT <= 10)) {
                s_cpClientFixer = getContentResolver().acquireContentProviderClient(CONFIG_CONTENT_URI);
            }
        }
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        ProcessCheck.CheckServiceProcess();

        String res = "";
        int nType = values.getAsInteger(EXTRA_TYPE);
        if (nType == TYPE_BOOLEAN) {
            res += SharedPrefConfigManager.getInstance(getContext()).getBoolean(
                    values.getAsString(EXTRA_KEY),
                    values.getAsBoolean(EXTRA_VALUE));
        } else if (nType == TYPE_STRING) {
            res += SharedPrefConfigManager.getInstance(getContext()).getString(
                    values.getAsString(EXTRA_KEY),
                    values.getAsString(EXTRA_VALUE));
        } else if (nType == TYPE_INT) {
            res += SharedPrefConfigManager.getInstance(getContext()).getInt(
                    values.getAsString(EXTRA_KEY),
                    values.getAsInteger(EXTRA_VALUE));
        } else if (nType == TYPE_LONG) {
            res += SharedPrefConfigManager.getInstance(getContext()).getLong(
                    values.getAsString(EXTRA_KEY),
                    values.getAsLong(EXTRA_VALUE));
        } else if (nType == TYPE_FLOAT) {
            res += SharedPrefConfigManager.getInstance(getContext()).getFloat(
                    values.getAsString(EXTRA_KEY),
                    values.getAsFloat(EXTRA_VALUE));
        }

        return Uri.parse(CONFIG_CONTENT_URI.toString() + "/" + res);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        ProcessCheck.CheckServiceProcess();

        int nType = values.getAsInteger(EXTRA_TYPE);
        if (nType == TYPE_BOOLEAN) {
            SharedPrefConfigManager.getInstance(getContext()).setBoolean(
                    values.getAsString(EXTRA_KEY),
                    values.getAsBoolean(EXTRA_VALUE));
        } else if (nType == TYPE_STRING) {
            SharedPrefConfigManager.getInstance(getContext()).setString(
                    values.getAsString(EXTRA_KEY),
                    values.getAsString(EXTRA_VALUE));
        } else if (nType == TYPE_INT) {
            SharedPrefConfigManager.getInstance(getContext()).setInt(
                    values.getAsString(EXTRA_KEY),
                    values.getAsInteger(EXTRA_VALUE));
        } else if (nType == TYPE_LONG) {
            SharedPrefConfigManager.getInstance(getContext()).setLong(
                    values.getAsString(EXTRA_KEY),
                    values.getAsLong(EXTRA_VALUE));
        } else if (nType == TYPE_FLOAT) {
            SharedPrefConfigManager.getInstance(getContext()).setFloat(
                    values.getAsString(EXTRA_KEY),
                    values.getAsFloat(EXTRA_VALUE));
        }
        return 1;
    }

    //------------------------------------------------------------------------//
    //                                                                        //
    //                  以下为是shared preference的读写封装                     //
    //                                                                        //
    //------------------------------------------------------------------------//
    public static boolean contains(String key){
        FixProviderSystemBug();

        ContentValues contetvalues = new ContentValues();
        contetvalues.put(EXTRA_TYPE, TYPE_LONG);
        contetvalues.put(EXTRA_KEY, key);
        contetvalues.put(EXTRA_VALUE, 0);
        Uri result = null;

        try {
            result = getContentResolver().insert(CONFIG_CONTENT_URI, contetvalues);
        } catch (IllegalArgumentException e) {
            return false;
        } catch (IllegalStateException e ) {
            return false;
        }

        if ( result == null ){
            return false;
        }

        return true;
    }

    public static void setBoolean(String key, boolean value) {
        ContentValues	contetvalues = new ContentValues();

        contetvalues.put(EXTRA_TYPE, TYPE_BOOLEAN);
        contetvalues.put(EXTRA_KEY, key);
        contetvalues.put(EXTRA_VALUE, value);

        FixProviderSystemBug();
        try {
            getContentResolver().update(CONFIG_CONTENT_URI, contetvalues, null, null);
        } catch (IllegalStateException e ) {
            e.printStackTrace();
        }
    }

    public static void setLong(String key, long value) {
        ContentValues	contetvalues = new ContentValues();

        contetvalues.put(EXTRA_TYPE, TYPE_LONG);
        contetvalues.put(EXTRA_KEY, key);
        contetvalues.put(EXTRA_VALUE, value);

        FixProviderSystemBug();
        try {
            getContentResolver().update(CONFIG_CONTENT_URI, contetvalues, null, null);
        } catch (IllegalStateException e ) {
            e.printStackTrace();
        }
    }

    public static void setInt(String key, int value) {
        ContentValues	contetvalues = new ContentValues();
        contetvalues.put(EXTRA_TYPE, TYPE_INT);
        contetvalues.put(EXTRA_KEY, key);
        contetvalues.put(EXTRA_VALUE, value);

        FixProviderSystemBug();
        try {
            getContentResolver().update(CONFIG_CONTENT_URI, contetvalues, null, null);
        } catch (IllegalStateException e ) {
            e.printStackTrace();
        }
    }


    public static void setFloat(String key, float value) {
        ContentValues	contetvalues = new ContentValues();
        contetvalues.put(EXTRA_TYPE, TYPE_FLOAT);
        contetvalues.put(EXTRA_KEY, key);
        contetvalues.put(EXTRA_VALUE, value);

        FixProviderSystemBug();
        try {
            getContentResolver().update(CONFIG_CONTENT_URI, contetvalues, null, null);
        } catch (IllegalStateException e ) {
            e.printStackTrace();
        }
    }

    public static void setString(String key, String value) {
        ContentValues	contetvalues = new ContentValues();
        contetvalues.put(EXTRA_TYPE, TYPE_STRING);
        contetvalues.put(EXTRA_KEY, key);
        contetvalues.put(EXTRA_VALUE, value);

        FixProviderSystemBug();
        try {
            getContentResolver().update(CONFIG_CONTENT_URI, contetvalues, null, null);
        } catch (IllegalStateException e ) {
            e.printStackTrace();
        }
    }


    public static long getLong(String key, long defValue){
        FixProviderSystemBug();

        ContentValues	contetvalues = new ContentValues();
        contetvalues.put(EXTRA_TYPE, TYPE_LONG);
        contetvalues.put(EXTRA_KEY, key);
        contetvalues.put(EXTRA_VALUE, defValue);
        Uri result = null;

        try {
            result = getContentResolver().insert(CONFIG_CONTENT_URI, contetvalues);
        } catch (IllegalArgumentException e) {
            return defValue;
        } catch (IllegalStateException e ) {
            return defValue;
        }

        if ( result == null ){
            return defValue;
        }else{
            String str = result.toString();
            if(TextUtils.isEmpty(str)){
                return defValue;
            }else if(str.length() <= LENGTH_CONTENT_URI){
                return defValue;
            }
        }

        return Long.valueOf(result.toString().substring(LENGTH_CONTENT_URI));
    }

    public static boolean getBoolean(String key, boolean defValue){
        FixProviderSystemBug();

        ContentValues	contetvalues = new ContentValues();
        contetvalues.put(EXTRA_TYPE, TYPE_BOOLEAN);
        contetvalues.put(EXTRA_KEY, key);
        contetvalues.put(EXTRA_VALUE, defValue);
        Uri result = null;

        try {
            result = getContentResolver().insert(CONFIG_CONTENT_URI, contetvalues);
        } catch (IllegalArgumentException e) {
            return defValue;
        }  catch (IllegalStateException e ) {
            return defValue;
        }

        if ( result == null )
            return defValue;

        return Boolean.valueOf(result.toString().substring(LENGTH_CONTENT_URI));
    }

    public static int getInt(String key, int defValue){
        FixProviderSystemBug();

        ContentValues	contetvalues = new ContentValues();
        contetvalues.put(EXTRA_TYPE, TYPE_INT);
        contetvalues.put(EXTRA_KEY, key);
        contetvalues.put(EXTRA_VALUE, defValue);
        Uri result = null;

        try {
            result = getContentResolver().insert(CONFIG_CONTENT_URI, contetvalues);
        } catch (IllegalArgumentException e) {
            return defValue;
        } catch (IllegalStateException e ) {
            return defValue;
        }

        if ( result == null )
            return defValue;

        return Integer.valueOf(result.toString().substring(LENGTH_CONTENT_URI));
    }

    public static float getFloat(String key, float defValue){
        FixProviderSystemBug();

        ContentValues	contetvalues = new ContentValues();
        contetvalues.put(EXTRA_TYPE, TYPE_FLOAT);
        contetvalues.put(EXTRA_KEY, key);
        contetvalues.put(EXTRA_VALUE, defValue);
        Uri result = null;

        try {
            result = getContentResolver().insert(CONFIG_CONTENT_URI, contetvalues);
        } catch (IllegalArgumentException e) {
            return defValue;
        } catch (IllegalStateException e ) {
            return defValue;
        }

        if ( result == null )
            return defValue;

        return Float.valueOf(result.toString().substring(LENGTH_CONTENT_URI));
    }

    public static String getString(String key, String defValue){
        FixProviderSystemBug();

        ContentValues	contetvalues = new ContentValues();
        contetvalues.put(EXTRA_TYPE, TYPE_STRING);
        contetvalues.put(EXTRA_KEY, key);
        contetvalues.put(EXTRA_VALUE, defValue);
        Uri result = null;

        try {
            result = getContentResolver().insert(CONFIG_CONTENT_URI, contetvalues);
        } catch (IllegalArgumentException e) {
            return defValue;
        } catch (IllegalStateException e ) {
            return defValue;
        }

        if ( result == null )
            return defValue;

        return String.valueOf(result.toString().substring(LENGTH_CONTENT_URI));
    }

}