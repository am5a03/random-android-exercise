package com.raymond.randomexercise.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by Raymond on 2016-04-04.
 */
public class ResponsiveIconView extends ImageView {

    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateDecelerateInterpolator ACCELERATE_DECELERATE_INTERPOLATOR = new AccelerateDecelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

    private boolean isPressed;

    public ResponsiveIconView(Context context) {
        super(context);
    }

    public ResponsiveIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ResponsiveIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                animate().scaleX(0.7f)
                        .scaleY(0.7f)
                         .setDuration(150)
                        .setInterpolator(DECCELERATE_INTERPOLATOR);
                isPressed = true;
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                boolean isInside = (x > 0 && x < getWidth()) && (y > 0 && y < getHeight());
                if (isPressed) {
                    isPressed = isInside;
                }
                break;
            case MotionEvent.ACTION_UP:
                animate().scaleX(1)
                        .scaleY(1)
                        .setInterpolator(DECCELERATE_INTERPOLATOR);
                if (isPressed) {
                    isPressed = false;
                }
                break;
        }
        return true;
    }
}
