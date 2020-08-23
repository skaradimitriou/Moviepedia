package com.stathis.moviepedia.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.MovieInfoScreen

import com.stathis.moviepedia.R
import com.stathis.moviepedia.TvSeriesInfoScreen
import com.stathis.moviepedia.models.Movies
import com.stathis.moviepedia.models.TvSeries
import com.stathis.moviepedia.models.TvSeriesFeed
import com.stathis.moviepedia.models.UpcomingMovies
import com.stathis.moviepedia.models.search.Query
import com.stathis.moviepedia.models.search.SearchItem
import com.stathis.moviepedia.models.search.SearchItemsFeed
import com.stathis.moviepedia.recyclerviews.AiringTvSeriesAdapter
import com.stathis.moviepedia.recyclerviews.QueryAdapter
import com.stathis.moviepedia.recyclerviews.SearchAdapter
import com.stathis.moviepedia.recyclerviews.SearchItemClickListener
import kotlinx.android.synthetic.main.fragment_search.*
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class SearchFragment : Fragment(),SearchItemClickListener {

    private lateinit var url: String
    private lateinit var request: Request
    private lateinit var client: OkHttpClient
    private var apiKey: String = "b36812048cc4b54d559f16a2ff196bc5"
    private lateinit var bundle: String
    private var searchItems: MutableList<SearchItem> = mutableListOf()
    private var userQueries: MutableList<Query> = mutableListOf()
    private lateinit var databaseReference: DatabaseReference
    private lateinit var searchRecView: RecyclerView
    private lateinit var query: Query

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchRecView = view.findViewById(R.id.searchResultsRecView)

        //getting the QUERY from the search bar
        bundle = this.arguments?.getString("QUERY").toString()
        query = Query(bundle)

        /*if bundle is empty means that the user didn't search for something
        so I will show him his recent searches in a vertical recycler view*/
        if(bundle == "null"){
            getRecentUserQueries()
        } else {
            Log.d("he searched",bundle)
            getQueryInfo(query)
        }

    }

    private fun getQueryInfo(query: Query) {
        //saving the user queries to the db so we can access them for an enhanced User Experience
        if (query.queryName != "null"){
            addQueryToDb(query)
        }

        //Building the url by replacing spacings with "+" so I can call the API for results
        if (query.queryName.contains(" ")) {
            query.queryName = query.queryName.replace("\\s".toRegex(), "+")
            url = "https://api.themoviedb.org/3/search/multi?api_key=$apiKey&query=${query.queryName}"
            Log.d("URL", url)
        } else {
            url = "https://api.themoviedb.org/3/search/multi?api_key=$apiKey&query=${query.queryName}"
            Log.d("URL", url)
        }

        request = Request.Builder().url(url).build()
        client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                //
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                Log.d("RESPONSE", body.toString())

                val searchItem = gson.fromJson(body, SearchItemsFeed::class.java)
                searchItems = ArrayList(searchItem.results)
                Log.d("TV", searchItems.toString())

                //passing data to the ui thread and displaying them
                activity!!.runOnUiThread {
                    searchRecView.adapter = SearchAdapter(searchItems as ArrayList<SearchItem>,this@SearchFragment)

                }

            }

        })
    }

    private fun addQueryToDb(query: Query) {
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("userSearchQueries")
            .child(query.queryName).setValue(query)
    }

    private fun getRecentUserQueries(){
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("userSearchQueries")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        for (i in p0.children) {
                            val query = i.getValue(Query::class.java)
                            Log.d("q",query.toString())
                            userQueries.add(query!!)
                            Log.d("movieList", userQueries.toString())

                            activity!!.runOnUiThread {
                                searchRecView.adapter = QueryAdapter(userQueries,this@SearchFragment)
                            }
                        }
                    }
                }
            })
    }


    override fun onQueryClick(query: Query) {
        queryTxt.visibility = View.GONE
        getQueryInfo(query)
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
