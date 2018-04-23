package com.example.sunjie.knowledgepointshareproject;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;

/**
 * Created by sunjie on 2018/4/18.
 */

public class JsonUtil {
    public static <T> T jsonToClazz(String json, Class<T> clazz)
    {
        if (json == null)
            return null;

        return JSON.parseObject(json, clazz);
    }

    public static String classToJson(Object obj)
    {
        return JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue);
    }

    public static <T> List<T> jsonToArray(String json, Class<T> clazz)
    {
        return JSON.parseArray(json, clazz);
    }
}
