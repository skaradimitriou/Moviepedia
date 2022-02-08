package com.stathis.moviepedia.ui.dashboard.fragments.profile

import android.content.Intent
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.stathis.moviepedia.R
import com.stathis.moviepedia.abstraction.AbstractBindingFragment
import com.stathis.moviepedia.databinding.FragmentProfileBinding
import com.stathis.moviepedia.listeners.FavoritesCallback
import com.stathis.moviepedia.models.movies.Movies
import com.stathis.moviepedia.models.series.TvSeries
import com.stathis.moviepedia.ui.movieDetails.MovieDetailsActivity
import com.stathis.moviepedia.ui.tvSeriesDetails.TvSeriesDetailsActivity

class ProfileFragment : AbstractBindingFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    private lateinit var viewModel: ProfileViewModel

    override fun init() {
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        binding.viewModel = viewModel
    }

    override fun startOps() {
        viewModel.getUserInfo()

        binding.userFavMovies.setOnClickListener {
            viewModel.getFavMovies()
        }

        binding.userFavTvSeries.setOnClickListener {
            viewModel.getFavTvSeries()
        }

        viewModel.observe(this, object : FavoritesCallback {
            override fun onMovieTap(model: Movies) {
                startActivity(Intent(activity, MovieDetailsActivity::class.java).also {
                    it.putExtra("MOVIE", model)
                })
            }

            override fun onTvSeriesTap(model: TvSeries) {
                startActivity(Intent(activity, TvSeriesDetailsActivity::class.java).apply {
                    putExtra("TV_SERIES", model)
                })
            }
        })

        viewModel.userImage.observe(this, Observer {
            Glide.with(this).load(it).error(R.drawable.moviepedia_logo).into(binding.profileUserImg)
        })

        viewModel.username.observe(this, Observer{
            binding.profileUserName.text = it
        })
    }

    override fun stopOps() {
        viewModel.release(this)
    }
}