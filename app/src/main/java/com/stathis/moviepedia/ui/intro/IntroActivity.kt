package com.stathis.moviepedia.ui.intro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.stathis.moviepedia.R
import com.stathis.moviepedia.abstraction.AbstractBindingActivity
import com.stathis.moviepedia.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {

    private lateinit var viewModel : IntroViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        supportActionBar?.hide()
        viewModel = ViewModelProvider(this).get(IntroViewModel::class.java)
    }
}