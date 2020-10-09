package com.stathis.moviepedia.ui.dashboard.fragments.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.stathis.moviepedia.ui.movieInfoScreen.MovieInfoScreen

import com.stathis.moviepedia.ui.tvSeriesInfoScreen.TvSeriesInfoScreen
import com.stathis.moviepedia.databinding.FragmentSearchBinding
import com.stathis.moviepedia.ui.dashboard.fragments.search.models.Query
import com.stathis.moviepedia.ui.dashboard.fragments.search.models.SearchItem
import com.stathis.moviepedia.adapters.SearchAdapter
import com.stathis.moviepedia.adapters.SearchItemClickListener


class SearchFragment : Fragment(), SearchItemClickListener {

    private lateinit var query: Query
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var binding: FragmentSearchBinding
    private var searchScreenViewModel = SearchScreenViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchAdapter = SearchAdapter(this@SearchFragment)

        //getting the QUERY from the search bar
        query = Query(this.arguments?.getString("QUERY").toString())
        Log.d("QUERY", query.toString())

        /*if bundle is empty means that the user didn't search for something
        so I will show him his recent searches in a vertical recycler view*/
        if (query.queryName == "null" || query.queryName == "" || query.queryName.isEmpty()) {
            searchScreenViewModel.getRecentUserQueries().observe(this,Observer<MutableList<Query>>{ queries ->
                Log.d("Queries",queries.toString())
                searchAdapter.submitList(queries as List<Any>?)
                searchAdapter.notifyDataSetChanged()
                binding.searchResultsRecView.adapter = searchAdapter
            })
        } else {
            startShimmer()
            searchScreenViewModel.getQueryInfo(query)
                .observe(this, Observer<MutableList<SearchItem>> { searchItem ->
                    Log.d("Search Item",searchItem.toString())
                    searchAdapter.submitList(searchItem as List<Any>?)
                    binding.searchResultsRecView.adapter = searchAdapter
                    searchAdapter.notifyDataSetChanged()
                })
        }
    }

    override fun onQueryClick(query: Query) {
        binding.queryTxt.text = "Results for ${query.queryName}"

        startShimmer()

        searchScreenViewModel.getQueryInfo(query)
            .observe(this, Observer<MutableList<SearchItem>> { searchItem ->
                Log.d("Search Item",searchItem.toString())
                searchAdapter.submitList(searchItem as List<Any>?)
                binding.searchResultsRecView.adapter = searchAdapter
                searchAdapter.notifyDataSetChanged()
            })
    }

    private fun startShimmer(){
        searchAdapter.submitList(searchScreenViewModel.setShimmer() as List<Any>?)
        binding.searchResultsRecView.adapter = searchAdapter
        searchAdapter.notifyDataSetChanged()
    }

    override fun onSearchItemClick(searchItem: SearchItem) {
        when (searchItem.media_type) {
            "movie" -> {
                startActivity(Intent(activity, MovieInfoScreen::class.java).apply{
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
                startActivity(Intent(activity, TvSeriesInfoScreen::class.java).apply{
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
            else -> {
                //
            }
        }
    }

}
