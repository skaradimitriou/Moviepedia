package com.stathis.moviepedia.ui.castDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.stathis.moviepedia.R

class CastDetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: CastDetailsViewModel
    private var actorID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cast_details)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CastDetailsViewModel::class.java)
    }

    override fun onPostResume() {
        super.onPostResume()

        actorID = intent.getIntExtra("ACTOR_ID", actorID)

        viewModel.getActorData(actorID)

        viewModel.actorData.observe(this, Observer {
            Log.d("", it.toString())
        })
    }
}