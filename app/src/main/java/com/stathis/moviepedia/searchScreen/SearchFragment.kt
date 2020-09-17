package com.stathis.moviepedia.searchScreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.movieInfoScreen.MovieInfoScreen

import com.stathis.moviepedia.tvSeriesInfoScreen.TvSeriesInfoScreen
import com.stathis.moviepedia.databinding.FragmentSearchBinding
import com.stathis.moviepedia.models.search.Query
import com.stathis.moviepedia.models.search.SearchItem
import com.stathis.moviepedia.models.search.SearchItemsFeed
import com.stathis.moviepedia.recyclerviews.QueryAdapter
import com.stathis.moviepedia.recyclerviews.SearchAdapter
import com.stathis.moviepedia.recyclerviews.SearchItemClickListener
import kotlinx.android.synthetic.main.fragment_search.*
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class SearchFragment : Fragment(), SearchItemClickListener {

    private lateinit var bundle: String
    private lateinit var query: Query
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

        //getting the QUERY from the search bar
        bundle = this.arguments?.getString("QUERY").toString()
        query = Query(bundle)

        //Observers
        searchScreenViewModel.getRecentUserQueries().observe(this,Observer<MutableList<Query>>{queries ->
            Log.d("Queries",queries.toString())
            binding.searchResultsRecView.adapter = QueryAdapter(queries,this@SearchFragment)
        })

        searchScreenViewModel.getQueryInfo(query)
            .observe(this, Observer<MutableList<SearchItem>> { searchItem ->
                Log.d("Search Item",searchItem.toString())
                binding.searchResultsRecView.adapter = SearchAdapter(searchItem as ArrayList<SearchItem>,this@SearchFragment)
            })

        /*if bundle is empty means that the user didn't search for something
        so I will show him his recent searches in a vertical recycler view*/
        if (query.queryName == "null" || query.queryName == "" || bundle =="" || bundle.isNullOrEmpty() || bundle.isNullOrBlank()) {
            searchScreenViewModel.getRecentUserQueries()
        } else {
            searchScreenViewModel.getQueryInfo(query)
        }
    }

    override fun onQueryClick(query: Query) {
        searchScreenViewModel.getQueryInfo(query)
    }

    override fun onSearchItemClick(searchItem: SearchItem) {
        when (searchItem.media_type) {
            "movie" -> {
                val movieIntent = Intent(activity, MovieInfoScreen::class.java)
                val name = searchItem.name
                val title = searchItem.title
                //converting rating toString() so I can pass it. Double was throwing error
                val rating = searchItem.vote_average.toString()
                Log.d("rating", rating)
                if (name.isNullOrBlank()) {
                    movieIntent.putExtra("MOVIE_NAME", title)
                    Log.d("Movie Name Clicked", title)
                } else {
                    movieIntent.putExtra("MOVIE_NAME", name)
                    Log.d("Movie Name Clicked", name)
                }
                movieIntent.putExtra("MOVIE_ID", searchItem.id)
                movieIntent.putExtra("MOVIE_PHOTO", searchItem.backdrop_path)
                movieIntent.putExtra("MOVIE_PHOTO", searchItem.poster_path)
                movieIntent.putExtra("RELEASE_DATE", searchItem.release_date)
                movieIntent.putExtra("DESCRIPTION", searchItem.overview)
                movieIntent.putExtra("RATING", rating)
                startActivity(movieIntent)
            }
            "tv" -> {
                val movieIntent = Intent(activity, TvSeriesInfoScreen::class.java)
                val name = searchItem.name
                val original_name = searchItem.original_name
                //converting rating toString() so I can pass it. Double was throwing error
                val rating = searchItem.vote_average.toString()
                Log.d("rating", rating)
                if (name.isNullOrBlank()) {
                    movieIntent.putExtra("TV_SERIES_NAME", original_name)
                    Log.d("Movie Name Clicked", original_name)
                } else {
                    movieIntent.putExtra("TV_SERIES_NAME", name)
                    Log.d("Movie Name Clicked", name)
                }

                if (searchItem.poster_path.isNullOrBlank()) {
                    movieIntent.putExtra("TV_SERIES_PHOTO", searchItem.backdrop_path)
                } else {
                    movieIntent.putExtra("TV_SERIES_PHOTO", searchItem.poster_path)
                }
                movieIntent.putExtra("TV_SERIES_ID", searchItem.id)
                movieIntent.putExtra("TV_SERIES_RELEASE_DATE", searchItem.first_air_date)
                movieIntent.putExtra("TV_SERIES_DESCRIPTION", searchItem.overview)
                movieIntent.putExtra("TV_SERIES_RATING", rating)
                startActivity(movieIntent)
            }
            else -> {
                //
            }
        }
    }

}
