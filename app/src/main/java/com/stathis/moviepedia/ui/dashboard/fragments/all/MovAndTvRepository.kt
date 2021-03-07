package com.stathis.moviepedia.ui.dashboard.fragments.all

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.API_KEY
import com.stathis.moviepedia.BASE_URL
import com.stathis.moviepedia.models.*
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class MovAndTvRepository {

    private lateinit var url: String
    private lateinit var request: Request
    private val client = OkHttpClient()
    private lateinit var databaseReference: DatabaseReference
    val upcomingMovies = MutableLiveData<List<Movies>>()
    val popularMovies = MutableLiveData<List<Movies>>()
    val movieGenres = MutableLiveData<List<MovieGenres>>()
    val topRatedMovies = MutableLiveData<List<Movies>>()
    val favoriteMovies = MutableLiveData<List<FavoriteMovies>>()
    private var userFavMovies: MutableList<FavoriteMovies> = mutableListOf()

    fun getUpcomingMovies() {
        url = "$BASE_URL/movie/upcoming?$API_KEY"
        request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string()
                val upcomingMoviesList =
                    GsonBuilder().create().fromJson(body, UpcomingMovies::class.java)
                upcomingMovies.postValue(upcomingMoviesList.results)
            }
        })
    }

    fun getTrendingMovies() {
        url = "$BASE_URL/trending/all/day?$API_KEY"
        request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                val popularMoviesList = gson.fromJson(body, MovieFeed::class.java)
                Log.d("Response", popularMoviesList.toString())
                popularMovies.postValue(popularMoviesList.results)
            }
        })
    }

    fun getTopRatedMovies() {
        url = "$BASE_URL/movie/top_rated?$API_KEY"
        request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Top Rated Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val topRatedMoviesList =
                    GsonBuilder().create().fromJson(body, MovieFeed::class.java)
                topRatedMovies.postValue(topRatedMoviesList.results)
            }
        })
    }

    fun getFavoriteMovies() {
        databaseReference = FirebaseDatabase.getInstance().reference
        //offline use
        databaseReference.keepSynced(true)
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("favoriteMovieList")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        /*viewModel was creating duplicated objects so I clear the list
                        if the list size contains elements inside*/
                        if (userFavMovies.isNotEmpty()) {
                            userFavMovies.clear()
                        }
                        //adding elements here
                        p0.children.forEach {
                            userFavMovies.add(it.getValue(FavoriteMovies::class.java)!!)
                        }
                    }
                    //sending them to the viewModel
                    favoriteMovies.postValue(userFavMovies)
                }
            })
    }

    fun getMovieGenres() {
        url = "$BASE_URL/genre/movie/list?$API_KEY"
        request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Genre call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val movieGenresList =
                    GsonBuilder().create().fromJson(body, MovieGenresFeed::class.java)
                movieGenres.postValue(movieGenresList.genres)
            }
        })
    }
}