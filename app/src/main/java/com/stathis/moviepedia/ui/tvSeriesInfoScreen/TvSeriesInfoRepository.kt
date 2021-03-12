package com.stathis.moviepedia.ui.tvSeriesInfoScreen

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.stathis.moviepedia.models.FavoriteTvSeries
import com.stathis.moviepedia.models.Reviews
import com.stathis.moviepedia.models.ReviewsFeed
import com.stathis.moviepedia.models.cast.Cast
import com.stathis.moviepedia.models.cast.MovieCastFeed
import com.stathis.moviepedia.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TvSeriesInfoRepository {

    private val databaseReference by lazy { FirebaseDatabase.getInstance().reference }
    val cast = MutableLiveData<List<Cast>>()
    val reviews = MutableLiveData<List<Reviews>>()
    val isFavorite = MutableLiveData<Boolean>()

    fun getCastInfo(tvSeriesId: Int) {
        ApiClient.getTvCastInfo(tvSeriesId).enqueue(object : Callback<MovieCastFeed> {
            override fun onResponse(call: Call<MovieCastFeed>, response: Response<MovieCastFeed>) {
                cast.value = response.body()?.cast
            }

            override fun onFailure(call: Call<MovieCastFeed>, t: Throwable) {
                cast.value = null
            }
        })
    }

    fun getTvSeriesReviews(tvSeriesId: Int) {
        ApiClient.getTvReviews(tvSeriesId).enqueue(object : Callback<ReviewsFeed> {
            override fun onResponse(call: Call<ReviewsFeed>, response: Response<ReviewsFeed>) {
                reviews.value = response.body()?.results
            }

            override fun onFailure(call: Call<ReviewsFeed>, t: Throwable) {
                reviews.value = null
            }
        })
    }

    fun addToFavorites(tvSeries: FavoriteTvSeries) {
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("favoriteTvSeries")
            .child(tvSeries.id.toString()).setValue(tvSeries)
        isFavorite.value = true
    }

    fun getUserFavorites(tvSeriesId: Int) {
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("favoriteTvSeries")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        p0.children.forEach {
                            val fav = it.getValue(FavoriteTvSeries::class.java)
                            if (fav?.id == tvSeriesId) {
                                isFavorite.value = true
                            }
                        }
                    }
                }
            })
    }

    fun removeFromFavorites(tvSeriesId: Int) {
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("favoriteTvSeries")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        p0.children.forEach {
                            val favorite = it.getValue(FavoriteTvSeries::class.java)
                            if (favorite?.id == tvSeriesId) {
                                it.ref.removeValue()
                                isFavorite.value = false
                            }
                        }
                    }
                }
            })
    }
}