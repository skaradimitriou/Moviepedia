package com.stathis.moviepedia.ui.movieInfoScreen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.models.FavoriteMovies
import com.stathis.moviepedia.models.Reviews
import com.stathis.moviepedia.models.ReviewsFeed
import com.stathis.moviepedia.models.cast.Cast
import com.stathis.moviepedia.models.cast.MovieCastFeed
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class MoviesInfoRepository {

    private lateinit var url: String
    private lateinit var request: Request
    private var client = OkHttpClient()
    val castInfo = MutableLiveData<List<Cast>>()
    val reviews = MutableLiveData<List<Reviews>>()
    val isFavorite = MutableLiveData<Boolean>()
    val apiKey = "api_key=b36812048cc4b54d559f16a2ff196bc5"
    private lateinit var databaseReference: DatabaseReference

    fun getMovieCastInfo(movieId: Int) {
        url =
            "https://api.themoviedb.org/3/movie/$movieId/credits?$apiKey"
        request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string()
                val cast = GsonBuilder().create().fromJson(body, MovieCastFeed::class.java)
                Log.d("Response", cast.toString())

                if (cast?.cast != null) {
                    castInfo.postValue(cast.cast)
                }
            }
        })
    }

    fun getMovieReviews(movieId: Int) {
        url =
            "https://api.themoviedb.org/3/movie/$movieId/reviews?$apiKey"
        request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string()
                val review = GsonBuilder().create().fromJson(body, ReviewsFeed::class.java)
                Log.d("Response", review.toString())

                if (review.results != null) {
                    reviews.postValue(review.results)
                }
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