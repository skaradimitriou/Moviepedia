package com.stathis.moviepedia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.LifecycleOwner
import com.stathis.moviepedia.loginAndRegister.IntroScreen

class SplashScreen : AppCompatActivity(), LifecycleOwner {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //4second splash time
        Handler().postDelayed({
            //start main activity
            startActivity(Intent(this, IntroScreen::class.java))
            //finish this activity
            finish()
            overridePendingTransition(0, 0)
        },5000)
    }
}