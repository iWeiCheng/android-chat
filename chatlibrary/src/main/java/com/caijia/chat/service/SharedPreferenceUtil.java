package com.caijia.chat.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by cai.jia on 2016/3/15 0004.
 */
public class SharedPreferenceUtil {

    public static Editor getEditor(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.edit();
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("chat_preferences", Context.MODE_PRIVATE);
    }
}
