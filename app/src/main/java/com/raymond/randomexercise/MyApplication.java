package com.raymond.randomexercise;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

/**
 * Created by Raymond on 2016-03-07.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
    }
}
