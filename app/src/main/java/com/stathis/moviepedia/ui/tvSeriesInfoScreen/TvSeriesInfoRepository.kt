package com.stathis.moviepedia.ui.tvSeriesInfoScreen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.API_KEY
import com.stathis.moviepedia.models.FavoriteTvSeries
import com.stathis.moviepedia.models.Reviews
import com.stathis.moviepedia.models.ReviewsFeed
import com.stathis.moviepedia.models.cast.Cast
import com.stathis.moviepedia.models.cast.MovieCastFeed
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class TvSeriesInfoRepository {

    private lateinit var url: String
    private lateinit var request: Request
    private var client: OkHttpClient = OkHttpClient()
    private lateinit var databaseReference: DatabaseReference
    val cast = MutableLiveData<MutableList<Cast>>()
    val reviews = MutableLiveData<MutableList<Reviews>>()
    val isFavorite = MutableLiveData<Boolean>()

    fun getCastInfo(tvSeriesId: Int) {
        url = "https://api.themoviedb.org/3/tv/$tvSeriesId/credits?api_key=$API_KEY"
        request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                val castFeed = gson.fromJson(body, MovieCastFeed::class.java)
                Log.d("Response", castFeed.toString())

                if (castFeed.cast != null) {
                    cast.postValue(ArrayList(castFeed.cast))
                }
            }
        })
    }

    fun getTvSeriesReviews(tvSeriesId: Int) {
        url = "https://api.themoviedb.org/3/tv/$tvSeriesId/reviews?api_key=$API_KEY"
        request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                val review = gson.fromJson(body, ReviewsFeed::class.java)
                Log.d("Response", review.toString())

                if (review.results != null) {
                    reviews.postValue(ArrayList(review.results))
                }
            }
        })
    }

    fun addToFavorites(tvSeries: FavoriteTvSeries) {
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("favoriteTvSeries")
            .child(tvSeries.id.toString()).setValue(tvSeries)
        isFavorite.value = true
    }

    fun getUserFavorites(tvSeriesId: Int) {
        databaseReference = FirebaseDatabase.getInstance().reference
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
        databaseReference = FirebaseDatabase.getInstance().reference
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