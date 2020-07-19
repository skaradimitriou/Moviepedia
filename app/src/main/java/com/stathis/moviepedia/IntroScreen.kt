package com.stathis.moviepedia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class IntroScreen : AppCompatActivity() {

    private lateinit var loginBtn:Button
    private lateinit var registerBtn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_screen)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        loginBtn.setOnClickListener{
            val login = Intent(this, SplashScreen::class.java)
            startActivity(login)
        }
        registerBtn.setOnClickListener{
            val register = Intent(this, SplashScreen::class.java)
            startActivity(register)
        }
    }
}
