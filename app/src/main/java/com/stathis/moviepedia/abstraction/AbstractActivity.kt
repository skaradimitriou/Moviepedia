package com.stathis.moviepedia.abstraction

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class AbstractActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initLayout()
    }

    abstract fun initLayout()

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onPostResume() {
        super.onPostResume()
        running()
    }

    abstract fun running()

    override fun onStop() {
        stopped()
        super.onStop()
    }

    abstract fun stopped()

    override fun onDestroy() {
        super.onDestroy()
    }
}