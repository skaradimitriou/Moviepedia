package com.stathis.moviepedia.ui.splash

import android.content.Intent
import android.os.Handler
import com.stathis.moviepedia.R
import com.stathis.moviepedia.abstraction.AbstractBindingActivity
import com.stathis.moviepedia.databinding.ActivityMainBinding
import com.stathis.moviepedia.ui.intro.IntroActivity

class SplashActivity : AbstractBindingActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun init() {
        supportActionBar?.hide()
    }

    override fun startOps() {
        Handler().postDelayed({
            startActivity(Intent(this, IntroActivity::class.java))
            finish()
        }, 1500)
    }

    override fun stopOps() {}
}