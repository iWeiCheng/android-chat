package com.caijia.chat.service;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by cai.jia on 2016/4/8 0008.
 */
public class JsonUtil {

    public static boolean isJsonObject(String json) throws Exception {
        if (TextUtils.isEmpty(json)) {
            return false;
        }
        Object o = new JSONTokener(json).nextValue();
        return o instanceof JSONObject;
    }

    public static boolean isJsonArray(String json) throws Exception {
        if (TextUtils.isEmpty(json)) {
            return false;
        }
        Object o = new JSONTokener(json).nextValue();
        return o instanceof JSONArray;
    }
}
