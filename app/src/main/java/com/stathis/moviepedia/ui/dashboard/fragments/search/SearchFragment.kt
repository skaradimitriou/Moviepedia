package com.stathis.moviepedia.ui.dashboard.fragments.search

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.stathis.moviepedia.abstraction.AbstractFragment
import com.stathis.moviepedia.ui.movieInfoScreen.MovieInfoScreen

import com.stathis.moviepedia.ui.tvSeriesInfoScreen.TvSeriesInfoScreen
import com.stathis.moviepedia.databinding.FragmentSearchBinding
import com.stathis.moviepedia.ui.dashboard.fragments.search.models.Query
import com.stathis.moviepedia.ui.dashboard.fragments.search.models.SearchItem
import com.stathis.moviepedia.adapters.SearchAdapter
import com.stathis.moviepedia.adapters.SearchItemClickListener


class SearchFragment : AbstractFragment(), SearchItemClickListener {

    private lateinit var query: Query
    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: SearchScreenViewModel

    override fun created(): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initLayout(view: View) {
        viewModel = ViewModelProvider(this).get(SearchScreenViewModel::class.java)
        viewModel.initListener(this)
    }

    override fun running() {
        query = Query(this.arguments?.getString("QUERY").toString())

        binding.searchResultsRecView.adapter = viewModel.searchAdapter

        if (query.queryName == "null" || query.queryName == "" || query.queryName.isEmpty()) {
            viewModel.getRecentUserQueries()
        } else {
            viewModel.getQueryInfo(query)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.observeData(this)
    }

    override fun stop() {
        viewModel.removeObservers(this)
    }

    override fun onQueryClick(query: Query) {
        viewModel.getQueryInfo(query)
        binding.queryTxt.text = "Results for ${query.queryName}"
    }

    override fun onSearchItemClick(searchItem: SearchItem) {
        when (searchItem.media_type) {
            "movie" -> {
                startActivity(Intent(activity, MovieInfoScreen::class.java).apply {
                    if (searchItem.name.isNullOrBlank()) {
                        putExtra("MOVIE_NAME", searchItem.title)
                        Log.d("Movie Name Clicked", searchItem.title)
                    } else {
                        putExtra("MOVIE_NAME", searchItem.name)
                        Log.d("Movie Name Clicked", searchItem.name)
                    }
                    putExtra("MOVIE_ID", searchItem.id)
                    putExtra("MOVIE_PHOTO", searchItem.backdrop_path)
                    putExtra("MOVIE_PHOTO", searchItem.poster_path)
                    putExtra("RELEASE_DATE", searchItem.release_date)
                    putExtra("DESCRIPTION", searchItem.overview)
                    putExtra("RATING", searchItem.vote_average.toString())
                })
            }
            "tv" -> {
                startActivity(Intent(activity, TvSeriesInfoScreen::class.java).apply {
                    if (searchItem.name.isNullOrBlank()) {
                        putExtra("TV_SERIES_NAME", searchItem.original_name)
                        Log.d("Movie Name Clicked", searchItem.original_name)
                    } else {
                        putExtra("TV_SERIES_NAME", searchItem.name)
                        Log.d("Movie Name Clicked", searchItem.name)
                    }

                    if (searchItem.poster_path.isNullOrBlank()) {
                        putExtra("TV_SERIES_PHOTO", searchItem.backdrop_path)
                    } else {
                        putExtra("TV_SERIES_PHOTO", searchItem.poster_path)
                    }
                    putExtra("TV_SERIES_ID", searchItem.id)
                    putExtra("TV_SERIES_RELEASE_DATE", searchItem.first_air_date)
                    putExtra("TV_SERIES_DESCRIPTION", searchItem.overview)
                    putExtra("TV_SERIES_RATING", searchItem.vote_average.toString())
                })
            }
        }
    }
}
