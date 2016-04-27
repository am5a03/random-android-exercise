package com.raymond.randomexercise.ui.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Raymond on 2016-04-28.
 */
public class FixedHeightSimpleDraweeView extends SimpleDraweeView {
    private static final String TAG = "FixedHeightSimpleDraweeView";
    private float myAspectRatio;
    public FixedHeightSimpleDraweeView(Context context) {
        super(context);
    }

    public FixedHeightSimpleDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixedHeightSimpleDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
//        Log.d(TAG, "onMeasure: " + w);
        h = (int)(w * myAspectRatio);
        setMeasuredDimension(w, h);
    }

    public void setMyAspectRatio(float myAspectRatio) {
        this.myAspectRatio = myAspectRatio;
    }
}
