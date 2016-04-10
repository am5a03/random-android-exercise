package com.raymond.randomexercise;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;

/**
 * Created by Raymond on 2016-04-04.
 */
public class AnimActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);

        View v = findViewById(R.id.img);
        if (v == null) return;

        v.setOnTouchListener(new DragListener());
    }

    private static class DragListener implements View.OnTouchListener {
        private static final String TAG = "DragListener";
        float dX = 0;
        float dY = 0;

        int[] loc = new int[2];

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            View pv = (View) v.getParent();
            int parentWidthBound = pv.getWidth() - v.getWidth();
            int parentHeightBound = pv.getHeight() - v.getHeight();

            float rawX = event.getRawX();
            float rawY = event.getRawY();

            v.getLocationOnScreen(loc);

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    dX = v.getX() - event.getRawX();
                    dY = v.getY() - event.getRawY();
                    Log.d(TAG, "onTouch: x=" + dX + ", y=" + dY);
                    break;
                case MotionEvent.ACTION_MOVE:

                    ViewPropertyAnimator animator = v.animate();

                    if (rawX + dX >= 0 && rawX + dX <= parentWidthBound) {
                        animator.x(rawX + dX);
                    }

                    if (rawY + dY >= 0 && rawY + dY <= parentHeightBound) {
                        animator.y(rawY + dY);
                    }

                    animator.setDuration(0).start();

                    Log.d(TAG, "onTouch: x=" + dX +
                            ", dY=" + dY +
                            ", diffX=" + (rawX + dX) +
                            ", diffY=" + (rawY + dY)
                    );

                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_CANCEL:
                    break;
            }
            return false;
        }
    }
}
