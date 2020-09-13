package com.stathis.moviepedia.mainScreen

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.models.*
import com.stathis.moviepedia.roomDatabase.DbMoviesDatabase
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class MovAndTvSeriesViewModel : ViewModel() {

    private lateinit var url: String
    private lateinit var request: Request
    private lateinit var client: OkHttpClient
    private lateinit var databaseReference: DatabaseReference
    private var userFavMovies: MutableList<FavoriteMovies> = mutableListOf()
    private var upcomingMoviesList: MutableList<Movies> = mutableListOf()
    private var trendingMoviesList: MutableList<Movies> = mutableListOf()
    private var topRatedMoviesList: MutableList<Movies> = mutableListOf()
    private var genresList: MutableList<MovieGenres> = mutableListOf()
    private var stream : MutableLiveData<MutableList<Movies>> = MutableLiveData()
    private var streamTwo : MutableLiveData<MutableList<Movies>> = MutableLiveData()
    private var streamGenres : MutableLiveData<MutableList<MovieGenres>> = MutableLiveData()
    private var streamThree : MutableLiveData<MutableList<Movies>> = MutableLiveData()
    private var favoriteMovies : MutableLiveData<MutableList<FavoriteMovies>> = MutableLiveData()

    fun UpComingMoviesCall(): MutableLiveData<MutableList<Movies>> {
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
                stream.postValue(upcomingMoviesList)
            }
        })
        return stream
    }

    fun TrendingMoviesCall(): MutableLiveData<MutableList<Movies>> {
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
                streamTwo.postValue(trendingMoviesList)
            }
        })
        return streamTwo
    }

    fun TopRatedMoviesCall(): MutableLiveData<MutableList<Movies>> {
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
                streamThree.postValue(topRatedMoviesList)
            }
        })
        return streamThree
    }

    fun getFavoriteMovies(): MutableLiveData<MutableList<FavoriteMovies>> {
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("favoriteMovieList")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()){
                        /*viewModel was creating duplicated objects so I clear the list
                        if the list size contains elements inside*/
                        if (userFavMovies.isNotEmpty()){
                            userFavMovies.clear()
                        }
                        //adding elements here
                        for (fav in p0.children){
                            userFavMovies.add(fav.getValue(FavoriteMovies::class.java)!!)
                        }
                    }
                    //sending them to the viewModel
                    favoriteMovies.postValue(userFavMovies)
                }
            })
        Log.d("fav ViewModel",favoriteMovies.toString())
        return favoriteMovies
    }

    fun getMovieGenres(): MutableLiveData<MutableList<MovieGenres>> {
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
                streamGenres.postValue(genresList)
            }
        })
        return streamGenres
    }
}