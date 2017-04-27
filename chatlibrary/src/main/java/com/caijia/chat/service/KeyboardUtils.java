package com.caijia.chat.service;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by cai.jia on 2015/11/25.
 */
public class KeyboardUtils {

    /**
     * 开启软键盘
     */
    public static void openSoftKeyboard(EditText et) {
        InputMethodManager inputManager = (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(et, 0);
    }

    /**
     * 关闭软键盘
     */
    public static void closeSoftKeyboard(EditText et) {
        InputMethodManager inputMethodManager = (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    public static void toggleSoftKeyboard(EditText et) {
        InputMethodManager inputMethodManager = (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(et.getWindowToken(), 0,0);
    }
}
