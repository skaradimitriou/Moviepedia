package com.stathis.moviepedia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //4second splash time
        Handler().postDelayed({
            //start main activity
            startActivity(Intent(this, IntroScreen::class.java))
            //finish this activity
            finish()
        },4000)
    }
}
