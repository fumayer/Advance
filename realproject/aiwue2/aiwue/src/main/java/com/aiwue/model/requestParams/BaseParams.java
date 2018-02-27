package com.aiwue.model.requestParams;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *   访问服务器参数基类
 *   created By Yibao on 2017年4月20日16:54:15
 *   Copyright (C) 2017 aiwue.com. All right reserved
 */

public class BaseParams {
      /**
     * 将实体类转换成请求参数,以map<k,v>形式返回
     *
     * @return
     */
    public Map<String, String> getMapParams() {
        Class<? extends BaseParams> clazz = this.getClass();
        Class<? extends Object> superclass = clazz.getSuperclass();

        Field[] fields = clazz.getDeclaredFields();
        Field[] superFields = superclass.getDeclaredFields();

        if (fields == null || fields.length == 0) {
            return Collections.emptyMap();
        }

        Map<String, String> params = new HashMap<String, String>();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.get(this) != null)
                    params.put(field.getName(), String.valueOf(field.get(this)));
            }

            for (Field superField : superFields) {
                superField.setAccessible(true);
                if (superField.get(this) != null)
                    params.put(superField.getName(), String.valueOf(superField.get(this)));
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        params.remove("serialVersionUID");
        return params;
    }
}
