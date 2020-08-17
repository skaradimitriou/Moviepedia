package com.stathis.moviepedia.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.GenresInfoScreen

import com.stathis.moviepedia.R
import com.stathis.moviepedia.TvSeriesInfoScreen
import com.stathis.moviepedia.models.*
import com.stathis.moviepedia.recyclerviews.*
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class TvSeriesFragment : Fragment(), ItemClickListener,GenresClickListener {

    private lateinit var url: String
    private lateinit var request: Request
    private lateinit var client: OkHttpClient
    private lateinit var database: DatabaseReference
    private val apiKey = "b36812048cc4b54d559f16a2ff196bc5"
    private var genresList: MutableList<MovieGenres> = mutableListOf()
    private lateinit var genresTvRecView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_series, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance().reference

        getFeaturedTvSeries()
        getAiringTodayTvSeries()
        getTvGenres()
        getTopRatedTvSeries()
        getPopularTvSeries()
    }

    private fun getFeaturedTvSeries() {
        url = "https://api.themoviedb.org/3/tv/on_the_air?api_key=b36812048cc4b54d559f16a2ff196bc5"
        request = Request.Builder().url(url).build()

        client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("call failed", call.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val gson = GsonBuilder().create()

                val featuredTvSeries =
                    gson.fromJson(response.body?.string(), TvSeriesFeed::class.java)
                //converts List to ArrayList<TvSeries>
                val testArray: ArrayList<TvSeries> = ArrayList(featuredTvSeries.results)
                Log.d("Response", testArray.toString())

                val featuredTvRecView: RecyclerView =
                    view!!.findViewById(R.id.upcomingTvSeriesRecView)

                activity!!.runOnUiThread {
                    featuredTvRecView.adapter =
                        FeaturedTvSeriesAdapter(testArray, this@TvSeriesFragment)
                }
            }

        })
    }

    private fun getAiringTodayTvSeries() {
        url =
            "https://api.themoviedb.org/3/tv/airing_today?api_key=b36812048cc4b54d559f16a2ff196bc5"
        request = Request.Builder().url(url).build()

        client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("call failed", call.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()

                val airingTodayTvSeries = gson.fromJson(body, TvSeriesFeed::class.java)
                Log.d("Response", airingTodayTvSeries.toString())

                //move from background to UI thread and display data
                val airingRecView: RecyclerView = view!!.findViewById(R.id.onTheAirRecView)

                activity!!.runOnUiThread {
                    airingRecView.adapter =
                        AiringTvSeriesAdapter(airingTodayTvSeries, this@TvSeriesFragment)
                }
            }
        })
    }

    private fun getTopRatedTvSeries() {
        url = "https://api.themoviedb.org/3/tv/top_rated?api_key=b36812048cc4b54d559f16a2ff196bc5"
        request = Request.Builder().url(url).build()

        client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("call failed", call.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()

                //converting body response from JSON to Kotlin class
                val topRatedTvSeries = gson.fromJson(body, TvSeriesFeed::class.java)
                Log.d("Response", topRatedTvSeries.toString())

                //move from background to UI thread and display data
                val topRatedTvRecView: RecyclerView = view!!.findViewById(R.id.topRatedTvRecView)
                activity!!.runOnUiThread {
                    topRatedTvRecView.adapter =
                        AiringTvSeriesAdapter(topRatedTvSeries, this@TvSeriesFragment)
                }
            }
        })
    }

    private fun getPopularTvSeries() {
        url =
            "https://api.themoviedb.org/3/tv/top_rated?api_key=b36812048cc4b54d559f16a2ff196bc5&language=en-US"
        request = Request.Builder().url(url).build()

        client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("call failed", call.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()

                //converting body response from JSON to Kotlin class
                val popularTvSeries = gson.fromJson(body, TvSeriesFeed::class.java)
                Log.d("Response", popularTvSeries.toString())

                //move from background to UI thread and display data
                val popularTvRecView: RecyclerView = view!!.findViewById(R.id.popularTvRecView)
                activity!!.runOnUiThread {
                    popularTvRecView.adapter =
                        AiringTvSeriesAdapter(popularTvSeries, this@TvSeriesFragment)
                }
            }
        })
    }

    private fun getTvGenres() {
        url =
            "https://api.themoviedb.org/3/genre/tv/list?api_key=$apiKey"
        request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Genre call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                val movieGenres = gson.fromJson(body, MovieGenresFeed::class.java)
                Log.d("RESPONSE", movieGenres.toString())
                genresList = ArrayList(movieGenres.genres)
                Log.d("RESPONSE", genresList.toString())

                genresTvRecView = view!!.findViewById(R.id.genresTvRecView)
                activity!!.runOnUiThread {
                    genresTvRecView.adapter =
                        GenresAdapter(genresList, this@TvSeriesFragment)
                }
            }
        })
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
