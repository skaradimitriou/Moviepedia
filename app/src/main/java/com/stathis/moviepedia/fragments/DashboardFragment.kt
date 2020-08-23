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
import com.stathis.moviepedia.GenresInfoScreen
import com.stathis.moviepedia.MovieInfoScreen
import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.*
import com.stathis.moviepedia.recyclerviews.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class DashboardFragment : Fragment(), ItemClickListener, GenresClickListener,FavoriteClickListener {

    private lateinit var url: String
    private lateinit var request: Request
    private lateinit var client: OkHttpClient
    private lateinit var databaseReference: DatabaseReference
    private var upcomingMoviesList: MutableList<Movies> = mutableListOf()
    private var trendingMoviesList: MutableList<Movies> = mutableListOf()
    private var genresList: MutableList<MovieGenres> = mutableListOf()
    private var topRatedMoviesList: MutableList<Movies> = mutableListOf()
    private var userFavMovies: MutableList<FavoriteMovies> = mutableListOf()
    private lateinit var listAdapter:ListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdapter = ListAdapter(this@DashboardFragment)

        getUpcomingMovies()
        getMovieGenres()
        getTopRatedMovies()
        getTrendingMovies()
        getFavoriteMovies()

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

                Log.d("this is the list", upcomingMoviesList.toString())

                activity!!.runOnUiThread {
                    upcomingMoviesRecView.adapter =
                        UpcomingMoviesAdapter(upcomingMoviesList, this@DashboardFragment)
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
                Log.d("this is the list", trendingMoviesList.toString())

                //move from background to ui thread and display data
                activity!!.runOnUiThread {
                    listAdapter.submitList(upcomingMoviesList)
                    popularRecView.adapter = listAdapter
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

                val gson = GsonBuilder().create()
                val movieGenres = gson.fromJson(body, MovieGenresFeed::class.java)
                Log.d("RESPONSE", movieGenres.toString())
                genresList = ArrayList(movieGenres.genres)


                activity!!.runOnUiThread {
                    genresRecView.adapter = GenresAdapter(genresList, this@DashboardFragment)
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
                Log.d("this is the list", topRatedMoviesList.toString())

                activity!!.runOnUiThread {
//                    sorting list by rating and passing it to the adapter
                    topRatedRecView.adapter = PopularMoviesAdapter(topRatedMoviesList.sortedWith(
                        compareBy { it.vote_average }).reversed() as MutableList<Movies>,
                        this@DashboardFragment)

                    Log.d("SortedList", topRatedMoviesList.sortedWith(
                        compareBy { it.vote_average }).reversed().toString()
                    )
                }
            }
        })
    }

    private fun getFavoriteMovies(){
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("favoriteMovieList")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        for (i in p0.children) {
                            val fav = i.getValue(FavoriteMovies::class.java)
                            userFavMovies.add(fav!!)
                            Log.d("FAV_MOVIES", userFavMovies.toString())
                            favoriteMoviesRV.adapter = FavoriteMoviesAdapter(userFavMovies,this@DashboardFragment)
                        }
                    } else {
                        userFav.visibility = View.GONE
                    }
                }

            })
    }

    /* handles movie clicks.
    * The point was to send the movie data to the movie info screen*/
    override fun onItemClick(movies: Movies) {
        val movieIntent = Intent(activity, MovieInfoScreen::class.java)
        val name = movies.name
        val title = movies.title
        //converting rating toString() so I can pass it. Double was throwing error
        val rating = movies.vote_average.toString()
        Log.d("rating", rating)
        if (name.isNullOrBlank()) {
            movieIntent.putExtra("MOVIE_NAME", title)
            Log.d("Movie Name Clicked", title)
        } else {
            movieIntent.putExtra("MOVIE_NAME", name)
            Log.d("Movie Name Clicked", name)
        }
        movieIntent.putExtra("MOVIE_ID", movies.id)
        movieIntent.putExtra("MOVIE_PHOTO", movies.backdrop_path)
        movieIntent.putExtra("MOVIE_PHOTO", movies.poster_path)
        movieIntent.putExtra("RELEASE_DATE", movies.release_date)
        movieIntent.putExtra("DESCRIPTION", movies.overview)
        movieIntent.putExtra("RATING", rating)
        startActivity(movieIntent)

    }

    override fun onTvSeriesClick(tvSeries: TvSeries) {
        TODO("Not yet implemented")
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

    override fun onFavoriteMoviesClick(favoriteMovies: FavoriteMovies) {
        val movieIntent = Intent (activity, MovieInfoScreen::class.java)
        //converting rating toString() so I can pass it. Double was throwing an error
        val rating = favoriteMovies.movie_rating.toString()
        movieIntent.putExtra("MOVIE_NAME", favoriteMovies.title)
        movieIntent.putExtra("MOVIE_PHOTO",favoriteMovies.photo)
        movieIntent.putExtra("RELEASE_DATE",favoriteMovies.releaseDate)
        movieIntent.putExtra("DESCRIPTION",favoriteMovies.description)
        movieIntent.putExtra("RATING",rating)
        startActivity(movieIntent)
    }

    override fun onFavoriteTvSeriesClick(favoriteTvSeries: FavoriteTvSeries) {
        //
    }



}
