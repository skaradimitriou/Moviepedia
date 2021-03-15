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
import java.text.SimpleDateFormat
import java.util.*

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
            actor_name.text = it.name
            Glide.with(this).load("$ORIGINAL_PHOTO_URL${it.profile_path}")
                .placeholder(R.drawable.default_img).into(main_actor_img)
            actor_description.text = it.biography
            actor_industry.text = it.known_for_department
            actor_description.text = it.place_of_birth

            val date = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(it.birthday)
            actor_birth.text = SimpleDateFormat("dd.MM.yyyy", Locale.US).format(date!!)
        })

        viewModel.observeData(this)
    }

    override fun stopped() {
        viewModel.actorData.removeObservers(this)
        viewModel.removeObservers(this)
    }
}