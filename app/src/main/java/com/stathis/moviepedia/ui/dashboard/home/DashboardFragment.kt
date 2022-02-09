package com.stathis.moviepedia.ui.dashboard.home

import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.stathis.moviepedia.R
import com.stathis.moviepedia.abstraction.AbstractBindingFragment
import com.stathis.moviepedia.abstraction.AbstractFragment
import com.stathis.moviepedia.ui.genres.GenresActivity
import com.stathis.moviepedia.ui.movieDetails.MovieDetailsActivity
import com.stathis.moviepedia.databinding.FragmentDashboardBinding
import com.stathis.moviepedia.listeners.HomeScreenCallback
import com.stathis.moviepedia.models.*
import com.stathis.moviepedia.listeners.old.FavoriteClickListener
import com.stathis.moviepedia.listeners.old.GenresClickListener
import com.stathis.moviepedia.listeners.old.ItemClickListener
import com.stathis.moviepedia.models.genres.MovieGenres
import com.stathis.moviepedia.models.movies.Movies
import com.stathis.moviepedia.models.movies.UpcomingMovies
import com.stathis.moviepedia.models.series.TvSeries


class DashboardFragment : AbstractBindingFragment<FragmentDashboardBinding>(R.layout.fragment_dashboard) {

    private lateinit var viewModel: MovAndTvSeriesViewModel

    override fun init() {
        viewModel = ViewModelProvider(this).get(MovAndTvSeriesViewModel::class.java)

        binding.viewModel = viewModel
    }

    override fun startOps() {
        viewModel.observeData(this, object : HomeScreenCallback{
            override fun onUpcomingMovieTap(model: UpcomingMovies) = openUpcomingMovie(model)
            override fun onMovieTap(model: Movies) = openMovie(model)
            override fun onGenreTap(model: MovieGenres) = openGenre(model)
        })
    }

    override fun stopOps() {
        viewModel.removeObservers(this)
    }

    private fun openMovie(movies: Movies) {
        startActivity(Intent(activity, MovieDetailsActivity::class.java).also {
            it.putExtra("MOVIE",movies)
        })
    }
    private fun openUpcomingMovie(movies: UpcomingMovies) {
        startActivity(Intent(activity, MovieDetailsActivity::class.java).also {
            //it.putExtra("MOVIE",movies)
        })
    }

    private fun openGenre(model: MovieGenres) {
        startActivity(Intent(activity, GenresActivity::class.java).apply {
            putExtra("GENRE_ID", model.id)
            putExtra("GENRE_NAME", model.name)
        })
    }
}