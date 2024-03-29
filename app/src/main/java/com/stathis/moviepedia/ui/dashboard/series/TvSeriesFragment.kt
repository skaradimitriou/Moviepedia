package com.stathis.moviepedia.ui.dashboard.series

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.stathis.moviepedia.abstraction.AbstractFragment
import com.stathis.moviepedia.listeners.old.GenresClickListener
import com.stathis.moviepedia.listeners.old.ItemClickListener
import com.stathis.moviepedia.ui.genres.GenresActivity

import com.stathis.moviepedia.ui.tvSeriesDetails.TvSeriesDetailsActivity
import com.stathis.moviepedia.databinding.FragmentTvSeriesBinding
import com.stathis.moviepedia.models.*
import com.stathis.moviepedia.models.genres.MovieGenres
import com.stathis.moviepedia.models.movies.Movies
import com.stathis.moviepedia.models.series.TvSeries

class TvSeriesFragment : AbstractFragment(), ItemClickListener, GenresClickListener {

    private lateinit var viewModel: TvSeriesViewModel
    private lateinit var binding: FragmentTvSeriesBinding

    override fun created(): View? {
        binding = FragmentTvSeriesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initLayout(view: View) {
        viewModel = ViewModelProvider(this).get(TvSeriesViewModel::class.java)

        viewModel.initListener(this, this)
    }

    override fun running() {
//        binding.upcomingTvSeriesRecView.adapter = viewModel.upcomingAdapter
//        binding.onTheAirRecView.adapter = viewModel.trendingAdapter
//        //binding.genresTvRecView.adapter = viewModel.genresAdapter
//        binding.topRatedTvRecView.adapter = viewModel.topRatedAdapter
//        binding.popularTvRecView.adapter = viewModel.airingAdapter

        //setShimmer()

        callApiForResults()
        viewModel.observeData(this)
    }

    override fun stop() {
        viewModel.removeObservers(this)
    }

    private fun callApiForResults() {
        viewModel.getFeaturedTvSeries()
        viewModel.getAiringTodayTvSeries()
        viewModel.getTvGenres()
        viewModel.getTopRatedTvSeries()
        viewModel.getPopularTvSeries()
    }

    private fun setShimmer() {
//        viewModel.upcomingAdapter.submitList(viewModel.setShimmer() as List<LocalModel>?)
//        viewModel.trendingAdapter.submitList(viewModel.setShimmer() as List<LocalModel>?)
//        viewModel.topRatedAdapter.submitList(viewModel.setShimmer() as List<LocalModel>?)
//        viewModel.genresAdapter.submitList(viewModel.setShimmer())
    }

    override fun onItemClick(movies: Movies) {}

    override fun onTvSeriesClick(tvSeries: TvSeries) {
        startActivity(Intent(activity, TvSeriesDetailsActivity::class.java).apply {
            putExtra("TV_SERIES", tvSeries)
        })

//            if (tvSeries.name.isNullOrBlank()) {
//                putExtra("TV_SERIES_NAME", tvSeries.original_name)
//                Log.d("Movie Name Clicked", tvSeries.original_name)
//            } else {
//                putExtra("TV_SERIES_NAME", tvSeries.name)
//                Log.d("Movie Name Clicked", tvSeries.name)
//            }
//
//            if (tvSeries.poster_path.isNullOrBlank()) {
//                putExtra("TV_SERIES_PHOTO", tvSeries.backdrop_path)
//            } else {
//                putExtra("TV_SERIES_PHOTO", tvSeries.poster_path)
//            }
//            putExtra("TV_SERIES_ID", tvSeries.id)
//            putExtra("TV_SERIES_RELEASE_DATE", tvSeries.first_air_date)
//            putExtra("TV_SERIES_DESCRIPTION", tvSeries.overview)
//            putExtra("TV_SERIES_RATING", tvSeries.vote_average.toString())
    }

    override fun onGenreClick(movieGenres: MovieGenres) {
        startActivity(Intent(activity, GenresActivity::class.java).apply {
            putExtra("GENRE_ID", movieGenres.id)
            putExtra("GENRE_NAME", movieGenres.name)
        })
    }
}