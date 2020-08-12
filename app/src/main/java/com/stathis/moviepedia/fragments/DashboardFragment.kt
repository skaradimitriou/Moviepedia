package com.stathis.moviepedia.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.GenresInfoScreen
import com.stathis.moviepedia.MovieInfoScreen

import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.*
import com.stathis.moviepedia.recyclerviews.*
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class DashboardFragment : Fragment(), ItemClickListener,GenresClickListener {

    private lateinit var url: String
    private lateinit var request: Request
    private lateinit var client: OkHttpClient
    private lateinit var databaseReference: DatabaseReference
    private var upcomingMoviesList: MutableList<Movies> = mutableListOf()
    private var trendingMoviesList: MutableList<Movies> = mutableListOf()
    private var genresList: MutableList<MovieGenres> = mutableListOf()
    private var topRatedMoviesList: MutableList<Movies> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUpcomingMovies()
        getMovieGenres()
        getTopRatedMovies()
        getTrendingMovies()
    }


    private fun getUpcomingMovies() {

        url = "https://api.themoviedb.org/3/movie/upcoming?api_key=b36812048cc4b54d559f16a2ff196bc5"
        request = Request.Builder().url(url).build()

        client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string()
                println(body)

                val gson = GsonBuilder().create()
                val upcomingMovies = gson.fromJson(body, UpcomingMovies::class.java)
                Log.d("Response", upcomingMovies.toString())
                upcomingMoviesList = ArrayList(upcomingMovies.results)

                Log.d("this is the list",upcomingMoviesList.toString())

                val upcomingMoviesRecView: RecyclerView = view!!.findViewById(R.id.upcomingMoviesRecView)

                activity!!.runOnUiThread {

                    upcomingMoviesRecView.adapter = UpcomingMoviesAdapter(upcomingMoviesList,this@DashboardFragment)
                }

            }
        })
    }
    private fun getTrendingMovies() {

        url =
            "https://api.themoviedb.org/3/trending/all/day?api_key=b36812048cc4b54d559f16a2ff196bc5"
        request = Request.Builder().url(url).build()

        client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                val popularMovies = gson.fromJson(body, MovieFeed::class.java)
                Log.d("Response", popularMovies.toString())

                trendingMoviesList = ArrayList(popularMovies.results)
                Log.d("this is the list",trendingMoviesList.toString())

                val popularRecView: RecyclerView = view!!.findViewById(R.id.popularRecView)
                //move from background to ui thread and display data
                activity!!.runOnUiThread {
                    popularRecView.adapter = PopularMoviesAdapter(trendingMoviesList,this@DashboardFragment)
                }
            }
        })
    }

    private fun getMovieGenres() {
        url =
            "https://api.themoviedb.org/3/genre/movie/list?api_key=b36812048cc4b54d559f16a2ff196bc5"
        request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Genre call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println(body)

                val gson = GsonBuilder().create()
                val movieGenres = gson.fromJson(body, MovieGenresFeed::class.java)
                Log.d("RESPONSE", movieGenres.toString())
                genresList = ArrayList(movieGenres.genres)

                val genresRecView: RecyclerView = view!!.findViewById(R.id.genresRecView)
                   activity!!.runOnUiThread {
                       genresRecView.adapter = GenresAdapter(genresList,this@DashboardFragment)
                   }

            }
        })
    }

    private fun getTopRatedMovies() {
        url =
            "https://api.themoviedb.org/3/movie/top_rated?api_key=b36812048cc4b54d559f16a2ff196bc5"
        request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Top Rated Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                Log.d("TOP RATED MOVIES CALL", body.toString())
                val gson = GsonBuilder().create()
                val topRatedMovies = gson.fromJson(body, MovieFeed::class.java)
                Log.d("Top Rated Call Response", topRatedMovies.toString())

                topRatedMoviesList = ArrayList(topRatedMovies.results)
                Log.d("this is the list",topRatedMoviesList.toString())

                val topRatedRecView: RecyclerView = view!!.findViewById(R.id.topRatedRecView)
                activity!!.runOnUiThread {
                    //sorting list by rating and passing it to the adapter
                    topRatedRecView.adapter = PopularMoviesAdapter(topRatedMoviesList.sortedWith(
                        compareBy {it.vote_average}).reversed() as MutableList<Movies>,this@DashboardFragment)
                    Log.d("SortedList",topRatedMoviesList.sortedWith(
                        compareBy {it.vote_average}).reversed().toString())
                }
            }
        })
    }

    /* handles movie clicks.
    * The point was to send the movie data to the movie info screen*/
    override fun onItemClick(movies: Movies) {
        val movieIntent = Intent (activity, MovieInfoScreen::class.java)
        val name = movies.name
        val title = movies.title
        //converting rating toString() so I can pass it. Double was throwing error
        val rating = movies.vote_average.toString()
        Log.d("rating", rating)
        if(name.isNullOrBlank()){
            movieIntent.putExtra("MOVIE_NAME", title)
            Log.d("Movie Name Clicked", title)
        } else {
            movieIntent.putExtra("MOVIE_NAME", name)
            Log.d("Movie Name Clicked", name)
        }
            movieIntent.putExtra("MOVIE_PHOTO",movies.backdrop_path)
        movieIntent.putExtra("MOVIE_PHOTO",movies.poster_path)
        movieIntent.putExtra("RELEASE_DATE",movies.release_date)
        movieIntent.putExtra("DESCRIPTION",movies.overview)
        movieIntent.putExtra("RATING",rating)
        startActivity(movieIntent)
    }

    override fun onTvSeriesClick(tvSeries: TvSeries) {
        TODO("Not yet implemented")
    }

    override fun onClick(v: View?) {
        //
    }

    override fun onGenreClick(movieGenres: MovieGenres) {
        val genresIntent = Intent (activity, GenresInfoScreen::class.java)
//        var genreId:Int = movieGenres.id
//        Log.d("genreid",genreId.toString())
//        for(i in genresList){
//            genreId = i.id
//            Log.d("genreid",genreId.toString())
//        }

        genresIntent.putExtra("GENRE_ID",movieGenres.id)
        genresIntent.putExtra("GENRE_NAME",movieGenres.name)
        startActivity(genresIntent)
    }


}
