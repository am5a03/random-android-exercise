package com.raymond.randomexercise.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;

import com.raymond.randomexercise.R;
import com.raymond.randomexercise.example.particle.*;
import com.raymond.randomexercise.example.photo.ActivityAnimations;
import com.raymond.robo.Metrics;

/**
 * Created by Raymond on 2016-04-04.
 */
public class AnimActivity extends AppCompatActivity {

    private static final String TAG = "AnimActivity";

    @Metrics
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);

        Log.d(TAG, "onCreate: " + Build.SERIAL + ", id=" + Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));
        Log.d(TAG, "onCreate: " + Build.FINGERPRINT);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        View v = findViewById(R.id.img);
        if (v == null) return;

        v.setOnTouchListener(new DragListener());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activities_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_1:
                startActivity(new Intent(this, ActivityAnimations.class));
                return true;
            case R.id.item_2:
                startActivity(new Intent(this, com.raymond.randomexercise.example.particle.MainActivity.class));
                return true;
            case R.id.item_3:
                startActivity(new Intent(this, PostListActivity.class));
                return true;
            case R.id.item_4:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
