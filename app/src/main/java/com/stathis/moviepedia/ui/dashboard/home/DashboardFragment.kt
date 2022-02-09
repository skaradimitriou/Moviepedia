package com.stathis.moviepedia.ui.dashboard.home

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.stathis.moviepedia.abstraction.AbstractFragment
import com.stathis.moviepedia.ui.genres.GenresActivity
import com.stathis.moviepedia.ui.movieDetails.MovieDetailsActivity
import com.stathis.moviepedia.databinding.FragmentDashboardBinding
import com.stathis.moviepedia.models.*
import com.stathis.moviepedia.listeners.old.FavoriteClickListener
import com.stathis.moviepedia.listeners.old.GenresClickListener
import com.stathis.moviepedia.listeners.old.ItemClickListener
import com.stathis.moviepedia.models.genres.MovieGenres
import com.stathis.moviepedia.models.movies.Movies
import com.stathis.moviepedia.models.series.TvSeries


class DashboardFragment : AbstractFragment(), ItemClickListener, GenresClickListener, FavoriteClickListener {

    private lateinit var viewModel: MovAndTvSeriesViewModel
    private lateinit var binding: FragmentDashboardBinding

    override fun created(): View? {
        binding = FragmentDashboardBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initLayout(view: View) {
        viewModel = ViewModelProvider(this).get(MovAndTvSeriesViewModel::class.java)
        viewModel.initCallbacks(this, this, this)
    }

    override fun running() {
        setShimmer()
        callApiForResults()

        binding.upcomingMoviesRecView.adapter = viewModel.upcomingAdapter
        binding.popularRecView.adapter = viewModel.trendingAdapter
        binding.genresRecView.adapter = viewModel.genresAdapter
        binding.topRatedRecView.adapter = viewModel.topRatedAdapter

        viewModel.observeData(this)
    }

    private fun callApiForResults() {
        viewModel.getUpcomingMovies()
        viewModel.getTrendingMovies()
        viewModel.getTopRatedMovies()
        viewModel.getFavoriteMovies()
        viewModel.getMovieGenres()
    }

    override fun stop() {
        viewModel.removeObservers(this)
    }

    private fun setShimmer() {
        viewModel.upcomingAdapter.submitList(viewModel.setShimmer() as List<LocalModel>?)
        viewModel.trendingAdapter.submitList(viewModel.setShimmer() as List<LocalModel>?)
        viewModel.topRatedAdapter.submitList(viewModel.setShimmer() as List<LocalModel>?)
        viewModel.genresAdapter.submitList(viewModel.setShimmer() as List<Any>?)
    }

    override fun onItemClick(movies: Movies) {
        startActivity(Intent(activity, MovieDetailsActivity::class.java).also {
            it.putExtra("MOVIE",movies)
        })
    }

    override fun onTvSeriesClick(tvSeries: TvSeries) {
        // N/A in this case | Refactor soon
    }

    override fun onGenreClick(movieGenres: MovieGenres) {
        startActivity(Intent(activity, GenresActivity::class.java).apply {
            putExtra("GENRE_ID", movieGenres.id)
            putExtra("GENRE_NAME", movieGenres.name)
        })
    }

    override fun onFavoriteMoviesClick(favoriteMovies: FavoriteMovies) {
        startActivity(Intent(activity, MovieDetailsActivity::class.java).apply {
            putExtra("MOVIE_ID", favoriteMovies.id)
            putExtra("MOVIE_NAME", favoriteMovies.title)
            putExtra("MOVIE_PHOTO", favoriteMovies.photo)
            putExtra("RELEASE_DATE", favoriteMovies.releaseDate)
            putExtra("DESCRIPTION", favoriteMovies.description)
            putExtra("RATING", favoriteMovies.movie_rating.toString())
        })
    }

    override fun onFavoriteTvSeriesClick(favoriteTvSeries: FavoriteTvSeries) {
        // N/A in this case | Refactor soon
    }
}