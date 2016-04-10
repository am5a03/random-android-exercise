package com.raymond.randomexercise.ui.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Raymond on 2016-03-18.
 */
public class NonSwipableViewPager extends ViewPager {
    public NonSwipableViewPager(Context context) {
        super(context);
    }

    public NonSwipableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
