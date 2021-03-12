package com.stathis.moviepedia.ui.movieInfoScreen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.stathis.moviepedia.models.FavoriteMovies
import com.stathis.moviepedia.models.Reviews
import com.stathis.moviepedia.models.ReviewsFeed
import com.stathis.moviepedia.models.cast.Cast
import com.stathis.moviepedia.models.cast.MovieCastFeed
import com.stathis.moviepedia.network.ApiClient
import retrofit2.Callback
import retrofit2.Response

class MoviesInfoRepository {

    val castInfo = MutableLiveData<List<Cast>>()
    val reviews = MutableLiveData<List<Reviews>>()
    val isFavorite = MutableLiveData<Boolean>()
    private lateinit var databaseReference: DatabaseReference

    fun getMovieCastInfo(movieId: Int) {
        ApiClient.getMovieCastInfo(movieId).enqueue(object : Callback<MovieCastFeed>{
            override fun onResponse(
                call: retrofit2.Call<MovieCastFeed>,
                response: Response<MovieCastFeed>
            ) {
                castInfo.value = response.body()?.cast
            }

            override fun onFailure(call: retrofit2.Call<MovieCastFeed>, t: Throwable) {
                castInfo.value = null
            }
        })
    }

    fun getMovieReviews(movieId: Int) {
        ApiClient.getMovieReviews(movieId).enqueue(object : Callback<ReviewsFeed>{
            override fun onResponse(
                call: retrofit2.Call<ReviewsFeed>,
                response: Response<ReviewsFeed>
            ) {
                reviews.value = response.body()?.results
            }

            override fun onFailure(call: retrofit2.Call<ReviewsFeed>, t: Throwable) {
                reviews.value = null
            }
        })
    }

    fun getFavoritesFromDb(movieTitle: String) {
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
                        p0.children.forEach {
                            val fav = it.getValue(FavoriteMovies::class.java)
                            if (fav?.title == movieTitle) {
                                isFavorite.value = true
                            }
                            Log.d("i", it.toString())
                        }
                    }
                }
            })
    }

    fun removeFromFavorites(movieTitle: String) {
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
                        p0.children.forEach {
                            val favorite = it.getValue(FavoriteMovies::class.java)
                            if (favorite?.title == movieTitle) {
                                it.ref.removeValue()
                                isFavorite.value = false
                            }
                        }
                    }
                }
            })
    }

    fun addToFavorites(movie: FavoriteMovies) {
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("favoriteMovieList")
            .child(movie.title).setValue(movie)
        isFavorite.value = true
    }
}