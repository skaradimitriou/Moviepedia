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
import com.stathis.moviepedia.recyclerviews.*

class TvSeriesFragment : Fragment(), ItemClickListener, GenresClickListener {


    private lateinit var database: DatabaseReference
    private var tvSeriesViewModel: TvSeriesViewModel =
        TvSeriesViewModel()
    private lateinit var binding: FragmentTvSeriesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTvSeriesBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance().reference

        tvSeriesViewModel = ViewModelProvider(this).get(TvSeriesViewModel::class.java)

        tvSeriesViewModel.getFeaturedTvSeries().observe(this,
            Observer<MutableList<TvSeries>> { t ->
                Log.d("Featured TvSeries",t.toString())
                val adapter = UpcomingAdapter(this@TvSeriesFragment)
                binding.upcomingTvSeriesRecView.adapter = adapter
                adapter.submitList(t as List<Any>?)
            })

        tvSeriesViewModel.getAiringTodayTvSeries().observe(this,
            Observer<MutableList<TvSeries>> { t -> binding.onTheAirRecView.adapter = AiringTvSeriesAdapter(t, this@TvSeriesFragment) })

        tvSeriesViewModel.getTopRatedTvSeries().observe(this,
            Observer<MutableList<TvSeries>> { t ->
                val topRatedAdapter:TopRatedAdapter = TopRatedAdapter( this@TvSeriesFragment)
                topRatedAdapter.submitList(t as List<Any>?)
                binding.topRatedTvRecView.adapter = topRatedAdapter
            })

        tvSeriesViewModel.getPopularTvSeries().observe(this,
            Observer<MutableList<TvSeries>> { t ->
                binding.popularTvRecView.adapter =
                    AiringTvSeriesAdapter(t, this@TvSeriesFragment)
            })

        tvSeriesViewModel.getTvGenres().observe(this,
            Observer<MutableList<MovieGenres>> { t ->
                binding.genresTvRecView.adapter =
                    GenresAdapter(t, this@TvSeriesFragment)
            })


            tvSeriesViewModel.getFeaturedTvSeries()
            tvSeriesViewModel.getAiringTodayTvSeries()
            tvSeriesViewModel.getTvGenres()
            tvSeriesViewModel.getTopRatedTvSeries()
            tvSeriesViewModel.getPopularTvSeries()
        }

    override fun onItemClick(movies: Movies) {
            //
        }

    override fun onTvSeriesClick(tvSeries: TvSeries) {
            val movieIntent = Intent(activity, TvSeriesInfoScreen::class.java)
            val name = tvSeries.name
            val original_name = tvSeries.original_name
            //converting rating toString() so I can pass it. Double was throwing error
            val rating = tvSeries.vote_average.toString()
            Log.d("rating", rating)
            if (name.isNullOrBlank()) {
                movieIntent.putExtra("TV_SERIES_NAME", original_name)
                Log.d("Movie Name Clicked", original_name)
            } else {
                movieIntent.putExtra("TV_SERIES_NAME", name)
                Log.d("Movie Name Clicked", name)
            }

            if (tvSeries.poster_path.isNullOrBlank()) {
                movieIntent.putExtra("TV_SERIES_PHOTO", tvSeries.backdrop_path)
            } else {
                movieIntent.putExtra("TV_SERIES_PHOTO", tvSeries.poster_path)
            }
            movieIntent.putExtra("TV_SERIES_ID", tvSeries.id)
            movieIntent.putExtra("TV_SERIES_RELEASE_DATE", tvSeries.first_air_date)
            movieIntent.putExtra("TV_SERIES_DESCRIPTION", tvSeries.overview)
            movieIntent.putExtra("TV_SERIES_RATING", rating)
            startActivity(movieIntent)
        }

    override fun onClick(v: View?) {
            //
        }

    override fun onGenreClick(movieGenres: MovieGenres) {
            val genresIntent = Intent(activity, GenresInfoScreen::class.java)
            genresIntent.putExtra("GENRE_ID", movieGenres.id)
            genresIntent.putExtra("GENRE_NAME", movieGenres.name)
            startActivity(genresIntent)
        }
    }
