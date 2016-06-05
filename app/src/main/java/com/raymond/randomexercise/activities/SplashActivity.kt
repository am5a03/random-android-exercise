//package com.raymond.randomexercise.activities
//
//import android.os.Bundle
//import android.support.v4.view.ViewCompat
//import android.support.v7.app.AppCompatActivity
//import android.view.View
//import android.view.animation.DecelerateInterpolator
//import android.widget.ImageView
//import android.widget.LinearLayout
//import com.raymond.randomexercise.R
//
///**
// * Created by Raymond on 2016-03-07.
// */
//class SplashActivity : AppCompatActivity() {
//
//    private var logo: ImageView? = null
//    private var container: LinearLayout? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_splash)
//
//        logo = findViewById(R.id.logo) as ImageView
//        container = findViewById(R.id.container) as LinearLayout
//
//        ViewCompat.animate(logo)
//            .translationY(-250f)
//            .setStartDelay(300)
//            .setDuration(1000)
//            .setInterpolator(DecelerateInterpolator(1.2f))
//            .start()
//
//        container?.visibility = View.GONE;
//    }
//}
