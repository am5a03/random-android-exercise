package com.raymond.robo;

import android.util.Log;

/**
 * Created by Raymond on 2016-04-24.
 */
public class MetricsLog {
    private MetricsLog(){}

    public static void logEvent(String action, String label, String value) {
        Log.d("MetricsLog", "action=" + action
                + ", label=" + label
                + ", value=" + value);
    }
}
