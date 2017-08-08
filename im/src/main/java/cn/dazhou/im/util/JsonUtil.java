package cn.dazhou.im.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created by hooyee on 2017/8/8.
 */

public class JsonUtil {
    private static Gson gson;

    public static <T> T parseJSON(String jsonString, Class<T> classType) {
        if (gson == null) {
            gson = new Gson();
        }
        T obj = null;
        try {
            obj = gson.fromJson(jsonString, classType);
        } catch (JsonSyntaxException e) {
            Log.e("TAG", "load json is fail");
        }
        return obj;
    }

    public static String toJSON(Object obj) {
        if (gson == null) {
            gson = new Gson();
        }
        String json = null;
        try {
            json = gson.toJson(obj);
            Log.i("TAG", "json-info = " + json);
        } catch (JsonSyntaxException e) {
            Log.e("TAG", "load json is fail");
        }
        return json;
    }
}
