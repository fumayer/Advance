package com.aiwue.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by bobsha on 17/1/11.
 */

public class GsonUtil {
    private static Gson gson_instance;

    private static void checkGson() {
        if (gson_instance == null) {
            gson_instance = new Gson();
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        T ret = null;
        checkGson();

        if (TextUtils.isEmpty(json)) {
            return ret;
        }

        try {
            ret = gson_instance.fromJson(json, clazz);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static <T> T fromJson(Reader in, Class<T> clazz) {
        if (in == null || clazz == null) {
            return null;
        }
        T ret = null;
        checkGson();


        try {
            ret = gson_instance.fromJson(in, clazz);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static <T> T fromJson(JsonReader in, Type clazz) {
        if (in == null || clazz == null) {
            return null;
        }
        T ret = null;
        checkGson();


        try {
            ret = gson_instance.fromJson(in, clazz);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return ret;
    }
    public static <T> T fromJson(JsonArray in, Type clazz) {
        if (in == null || clazz == null) {
            return null;
        }
        T ret = null;
        checkGson();


        try {
            ret = gson_instance.fromJson(in, clazz);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return ret;
    }
    public static <T> T fromJson(JsonObject in, Class<T> clazz) {
        if (in == null || clazz == null) {
            return null;
        }
        T ret = null;
        checkGson();


        try {
            ret = gson_instance.fromJson(in, clazz);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return ret;
    }


    public static <T> String toJson(T obj) {
        String ret = null;
        checkGson();

        if (obj == null) {
            return ret;
        }


        try {
            ret = gson_instance.toJson(obj);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static <T> ArrayList<T> fromJsonArray(String jsonArray, Type typeOfT) {
        ArrayList<T> list = new ArrayList<T>();
        checkGson();

        if (jsonArray == null) {
            return list;
        }


        try {
            list = gson_instance.fromJson(jsonArray, typeOfT);
        } catch (Throwable e) {
            Timber.e(e);
        }

        return list;
    }
}
