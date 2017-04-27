package com.caijia.chat.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.caijia.chat.R;

/**
 * Created by cai.jia on 2015/11/26.
 */
public class CircleIndicator extends View implements ViewPager.OnPageChangeListener {

    private int mRadius;

    private int mSpace;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public CircleIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public CircleIndicator(Context context) {
        this(context, null);
    }

    public CircleIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleIndicator);
        try {
            mRadius = a.getDimensionPixelOffset(R.styleable.CircleIndicator_radius, dp2px(5));
            int selectedColor = a.getColor(R.styleable.CircleIndicator_selectedColor, 0xff33b5e5);
            int normalColor = a.getColor(R.styleable.CircleIndicator_normalColor, 0xff868686);
            mSpace = a.getDimensionPixelOffset(R.styleable.CircleIndicator_space, dp2px(12));

            mSelectedPaint.setColor(selectedColor);
            mNormalPaint.setColor(normalColor);
        } finally {
            a.recycle();
        }
    }

    public void setViewPager(ViewPager view) {
        setViewPager(view, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = circleDotCount * mRadius * 2 + mSpace * (circleDotCount - 1);
        int height = mRadius * 2;
        setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(height, heightMeasureSpec));
    }

    private Paint mSelectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mNormalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int mSelectedPosition;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int actualWidth = circleDotCount * mRadius * 2 + mSpace * (circleDotCount - 1);
        int leftOffset = (width - actualWidth) / 2;
        int topOffset = (height - mRadius * 2) / 2;

        for (int i = 0; i < circleDotCount && circleDotCount > 1; i++) {
            canvas.drawCircle(leftOffset + mRadius * (i * 2 + 1) + i * mSpace, topOffset + mRadius, mRadius,
                    mSelectedPosition == i ? mSelectedPaint : mNormalPaint);
        }
    }

    private int circleDotCount;

    private ViewPager mViewPager;

    public void setCircleDotCount(int count) {
        circleDotCount = count;
    }

    public void setViewPager(ViewPager view, int initialPosition) {
        if (view == null) {
            return;
        }

        if (view.getAdapter() == null) {
            return;
        }

        mViewPager = view;
        if (circleDotCount == 0) {
            circleDotCount = view.getAdapter().getCount();
        }
        view.addOnPageChangeListener(this);
        view.setCurrentItem(initialPosition % circleDotCount);
    }

    public void setCurrentItem(int item) {
        mSelectedPosition = item % circleDotCount;
        mViewPager.setCurrentItem(mSelectedPosition);
        invalidate();
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mViewPager.addOnPageChangeListener(listener);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mSelectedPosition = position % circleDotCount;
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private int dp2px(int dpValue) {
        return (int) Math.ceil(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue,
                getContext().getResources().getDisplayMetrics()));
    }
}
