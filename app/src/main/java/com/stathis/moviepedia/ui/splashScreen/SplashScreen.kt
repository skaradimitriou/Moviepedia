package com.stathis.moviepedia.ui.splashScreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.stathis.moviepedia.R
import com.stathis.moviepedia.ui.loginAndRegister.IntroScreen

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler().postDelayed({
            startActivity(Intent(this, IntroScreen::class.java))
            finish()
            overridePendingTransition(0, 0)
        }, 5000)
    }
}