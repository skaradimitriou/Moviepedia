package com.stathis.moviepedia.ui.dashboard.fragments.tvSeries

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.stathis.moviepedia.ui.genresInfoScreen.GenresInfoScreen

import com.stathis.moviepedia.ui.tvSeriesInfoScreen.TvSeriesInfoScreen
import com.stathis.moviepedia.databinding.FragmentTvSeriesBinding
import com.stathis.moviepedia.models.*
import com.stathis.moviepedia.adapters.*

class TvSeriesFragment : Fragment(), ItemClickListener, GenresClickListener {

    private lateinit var upcomingAdapter: UpcomingAdapter
    private lateinit var trendingAdapter: TrendingAdapter
    private lateinit var topRatedAdapter: TopRatedAdapter
    private lateinit var genresAdapter: GenresAdapter
    private lateinit var database: DatabaseReference
    private var tvSeriesViewModel: TvSeriesViewModel =
        TvSeriesViewModel()
    private lateinit var binding: FragmentTvSeriesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tvSeriesViewModel = ViewModelProvider(this).get(TvSeriesViewModel::class.java)
        // Inflate the layout for this fragment
        binding = FragmentTvSeriesBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance().reference

        upcomingAdapter = UpcomingAdapter(this@TvSeriesFragment)
        binding.upcomingTvSeriesRecView.adapter = upcomingAdapter

        trendingAdapter = TrendingAdapter(this@TvSeriesFragment)
        binding.onTheAirRecView.adapter = trendingAdapter

        genresAdapter = GenresAdapter(this@TvSeriesFragment)
        binding.genresTvRecView.adapter = genresAdapter

        topRatedAdapter = TopRatedAdapter(this@TvSeriesFragment)
        binding.topRatedTvRecView.adapter = topRatedAdapter

        setShimmer()
        observeData()
    }

    private fun observeData() {
        tvSeriesViewModel.getFeaturedTvSeries().observe(this,
            Observer<MutableList<TvSeries>> { t ->
                Log.d("Featured TvSeries", t.toString())
                upcomingAdapter.submitList(t as List<Any>?)
                upcomingAdapter.notifyDataSetChanged()
            })

        tvSeriesViewModel.getAiringTodayTvSeries().observe(this,
            Observer<MutableList<TvSeries>> { t ->
                trendingAdapter.submitList(t as List<Any>?)
                trendingAdapter.notifyDataSetChanged()
            })

        tvSeriesViewModel.getTopRatedTvSeries().observe(this,
            Observer<MutableList<TvSeries>> { t ->
                topRatedAdapter.submitList(t as List<Any>?)
                topRatedAdapter.notifyDataSetChanged()
            })

        tvSeriesViewModel.getPopularTvSeries().observe(this,
            Observer<MutableList<TvSeries>> { t ->
                binding.popularTvRecView.adapter =
                    AiringTvSeriesAdapter(t, this@TvSeriesFragment)
            })

        tvSeriesViewModel.getTvGenres().observe(this,
            Observer<MutableList<MovieGenres>> { t ->
                genresAdapter.submitList(t as List<Any>?)
                genresAdapter.notifyDataSetChanged()
            })


        tvSeriesViewModel.getFeaturedTvSeries()
        tvSeriesViewModel.getAiringTodayTvSeries()
        tvSeriesViewModel.getTvGenres()
        tvSeriesViewModel.getTopRatedTvSeries()
        tvSeriesViewModel.getPopularTvSeries()
    }

    private fun setShimmer() {
        upcomingAdapter.submitList(tvSeriesViewModel.setShimmer() as List<Any>?)
        trendingAdapter.submitList(tvSeriesViewModel.setShimmer() as List<Any>?)
        topRatedAdapter.submitList(tvSeriesViewModel.setShimmer() as List<Any>?)
        genresAdapter.submitList(tvSeriesViewModel.setShimmer() as List<Any>?)
    }

    override fun onItemClick(movies: Movies) {
        //
    }

    override fun onTvSeriesClick(tvSeries: TvSeries) {
        startActivity(Intent(activity, TvSeriesInfoScreen::class.java).apply{
            if (tvSeries.name.isNullOrBlank()) {
                putExtra("TV_SERIES_NAME", tvSeries.original_name)
                Log.d("Movie Name Clicked", tvSeries.original_name)
            } else {
                putExtra("TV_SERIES_NAME", tvSeries.name)
                Log.d("Movie Name Clicked", tvSeries.name)
            }

            if (tvSeries.poster_path.isNullOrBlank()) {
                putExtra("TV_SERIES_PHOTO", tvSeries.backdrop_path)
            } else {
                putExtra("TV_SERIES_PHOTO", tvSeries.poster_path)
            }
            putExtra("TV_SERIES_ID", tvSeries.id)
            putExtra("TV_SERIES_RELEASE_DATE", tvSeries.first_air_date)
            putExtra("TV_SERIES_DESCRIPTION", tvSeries.overview)
            putExtra("TV_SERIES_RATING", tvSeries.vote_average.toString())
        })
    }

    override fun onClick(v: View?) {
        //
    }

    override fun onGenreClick(movieGenres: MovieGenres) {
        startActivity(Intent(activity, GenresInfoScreen::class.java).apply{
            putExtra("GENRE_ID", movieGenres.id)
            putExtra("GENRE_NAME", movieGenres.name)
        })
    }
}
