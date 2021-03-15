package com.stathis.moviepedia.ui.castDetails

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.stathis.moviepedia.BASE_URL
import com.stathis.moviepedia.ORIGINAL_PHOTO_URL
import com.stathis.moviepedia.R
import com.stathis.moviepedia.abstraction.AbstractActivity
import kotlinx.android.synthetic.main.activity_cast_details.*

class CastDetailsActivity : AbstractActivity() {

    private lateinit var viewModel: CastDetailsViewModel
    private var actorID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cast_details)
    }

    override fun initLayout() {
        viewModel = ViewModelProvider(this).get(CastDetailsViewModel::class.java)
    }

    override fun running() {
        actorID = intent.getIntExtra("ACTOR_ID", actorID)

        viewModel.getActorData(actorID)
        viewModel.getActorsKnownMovies(actorID)

        known_movies_recycler.adapter = viewModel.adapter

        viewModel.actorData.observe(this, Observer {
            Log.d("", it.toString())
            actor_name.text = it.name
            Glide.with(this).load("$ORIGINAL_PHOTO_URL${it.profile_path}").into(main_actor_img)
            actor_description.text = it.biography
        })

        viewModel.observeData(this)
    }

    override fun stopped() {
        viewModel.actorData.removeObservers(this)
        viewModel.removeObservers(this)
    }
}