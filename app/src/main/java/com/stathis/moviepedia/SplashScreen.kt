package com.stathis.moviepedia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.stathis.moviepedia.loginAndRegister.IntroScreen
import com.stathis.moviepedia.roomDatabase.DbMovies
import com.stathis.moviepedia.roomDatabase.DbMoviesDao
import com.stathis.moviepedia.roomDatabase.DbMoviesDatabase

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