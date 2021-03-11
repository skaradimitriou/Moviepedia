package com.stathis.moviepedia.ui.dashboard.fragments.all

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.stathis.moviepedia.models.*
import com.stathis.moviepedia.network.RetrofitApiClient
import retrofit2.Callback

class MovAndTvRepository {

    private val databaseReference by lazy { FirebaseDatabase.getInstance().reference }
    val upcomingMovies = MutableLiveData<List<Movies>>()
    val trendingMovies = MutableLiveData<List<Movies>>()
    val movieGenres = MutableLiveData<List<MovieGenres>>()
    val topRatedMovies = MutableLiveData<List<Movies>>()
    val favoriteMovies = MutableLiveData<List<FavoriteMovies>>()
    private var userFavMovies: MutableList<FavoriteMovies> = mutableListOf()

    fun getUpcomingMovies() {
        RetrofitApiClient.getCountries().enqueue(object : Callback<UpcomingMovies> {
            override fun onResponse(
                call: retrofit2.Call<UpcomingMovies>,
                response: retrofit2.Response<UpcomingMovies>
            ) {
                Log.d("", response.body().toString())
                upcomingMovies.postValue(response.body()?.results)
            }

            override fun onFailure(call: retrofit2.Call<UpcomingMovies>, t: Throwable) {
                upcomingMovies.postValue(null)
            }
        })
    }

    fun getTrendingMovies() {
        RetrofitApiClient.getTrendingMovies().enqueue(object : Callback<UpcomingMovies> {
            override fun onResponse(
                call: retrofit2.Call<UpcomingMovies>,
                response: retrofit2.Response<UpcomingMovies>
            ) {
                Log.d("", response.body().toString())
                trendingMovies.postValue(response.body()?.results)
            }

            override fun onFailure(call: retrofit2.Call<UpcomingMovies>, t: Throwable) {
                trendingMovies.postValue(null)
            }
        })
    }

    fun getMovieGenres() {
        RetrofitApiClient.getMovieGenres().enqueue(object : Callback<MovieGenresFeed> {
            override fun onResponse(
                call: retrofit2.Call<MovieGenresFeed>,
                response: retrofit2.Response<MovieGenresFeed>
            ) {
                Log.d("", response.body().toString())
                movieGenres.postValue(response.body()?.genres)
            }

            override fun onFailure(call: retrofit2.Call<MovieGenresFeed>, t: Throwable) {
                movieGenres.postValue(null)
            }
        })
    }

    fun getTopRatedMovies() {
        RetrofitApiClient.getTopRatedMovies().enqueue(object : Callback<UpcomingMovies> {
            override fun onResponse(
                call: retrofit2.Call<UpcomingMovies>,
                response: retrofit2.Response<UpcomingMovies>
            ) {
                Log.d("", response.body().toString())
                topRatedMovies.postValue(response.body()?.results)
            }

            override fun onFailure(call: retrofit2.Call<UpcomingMovies>, t: Throwable) {
                topRatedMovies.postValue(null)
            }
        })
    }

    fun getFavoriteMovies() {
        databaseReference.keepSynced(true) //offline use
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("favoriteMovieList")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    favoriteMovies.postValue(null)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        /*viewModel was creating duplicated objects so I clear the list
                        if the list size contains elements inside*/
                        if (userFavMovies.isNotEmpty()) {
                            userFavMovies.clear()
                        }

                        p0.children.forEach {
                            userFavMovies.add(it.getValue(FavoriteMovies::class.java)!!)
                        }
                    }
                    //sending them to the viewModel
                    favoriteMovies.postValue(userFavMovies)
                }
            })
    }
}