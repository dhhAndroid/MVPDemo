package com.dhh.mvp.base.mvp;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by dhh on 2017/5/22.
 */

public class GsonUtlis {
    public static final Gson GSON = new Gson();

    public static <K, V> Map<K, V> jsonToMap(String json, Class<K> key, Class<V> value) {
        return GSON.fromJson(json, new TypeToken<Map<K, V>>() {
        }.getType());
    }

    public static String toJson(Object bean) {
        return GSON.toJson(bean);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    public static <T> T fromJson(String json, Type type) {
        return GSON.fromJson(json, type);
    }

    public static <T> T fromJson(String json, T type) {
        try {
            Method method = GsonUtlis.class.getMethod("jsonToMap", String.class, Class.class, Class.class);
            Type type1 = method.getGenericReturnType();

            Log.d("GsonUtlis", "type1:" + type1);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return GSON.fromJson(json, TypeToken.get(type.getClass()).getType());
    }
}
