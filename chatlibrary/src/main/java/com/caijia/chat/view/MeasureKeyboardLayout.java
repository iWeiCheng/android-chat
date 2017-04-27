package com.caijia.chat.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 用来测量软键盘高度的layout
 * 必须是根布局
 * 高度 match_parent
 * android:windowSoftInputMode="adjustResize"
 */
public class MeasureKeyboardLayout extends RelativeLayout {

    private int mMaxHeight;

    private int mSoftKeyboardHeight;

    private boolean mOpenKeyboard;

    private List<Integer> mHeightList = new ArrayList<>();

    public MeasureKeyboardLayout(Context context) {
        super(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MeasureKeyboardLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public MeasureKeyboardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MeasureKeyboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mMaxHeight == 0) {
            mMaxHeight = h;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = MeasureSpec.getSize(heightMeasureSpec);
        mHeightList.add(size);
        if (mMaxHeight != 0) {
            int mode = MeasureSpec.getMode(heightMeasureSpec);
            int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, mode);
            super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mHeightList.size() > 1) {
            int oldHeight = mHeightList.get(0);
            int newHeight = mHeightList.get(mHeightList.size() - 1);
            if (mSoftKeyboardHeight == 0) {
                mSoftKeyboardHeight = mMaxHeight - newHeight;
            }

            if (mSoftKeyboardListener != null) {
                mOpenKeyboard = oldHeight == mMaxHeight;
                mSoftKeyboardListener.onSoftKeyboardStateChange(mOpenKeyboard, mSoftKeyboardHeight);
            }
        }
        mHeightList.clear();
    }

    private OnSoftKeyboardStateChangeListener mSoftKeyboardListener;

    public void setSoftKeyboardChangeListener(OnSoftKeyboardStateChangeListener l) {
        mSoftKeyboardListener = l;
    }

    public interface OnSoftKeyboardStateChangeListener {
        void onSoftKeyboardStateChange(boolean open, int softKeyboardHeight);
    }

    /**
     * 是否打开软键盘
     *
     * @return
     */
    public boolean isOpenSoftKb() {
        return mOpenKeyboard;
    }

    /**
     * 获取软键盘高度，当软键盘第一次打开后才能得到正确的高度
     *
     * @return
     */
    public int getSoftKbHeight() {
        return mSoftKeyboardHeight;
    }
}
