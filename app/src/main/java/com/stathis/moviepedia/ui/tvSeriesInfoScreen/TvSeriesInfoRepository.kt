package com.stathis.moviepedia.ui.tvSeriesInfoScreen

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.stathis.moviepedia.models.FavoriteTvSeries
import com.stathis.moviepedia.network.ApiClient

class TvSeriesInfoRepository {

    private lateinit var databaseReference: DatabaseReference
    val cast = ApiClient.cast
    val reviews = ApiClient.reviews
    val isFavorite = MutableLiveData<Boolean>()

    fun getCastInfo(tvSeriesId: Int) {
        ApiClient.getCastInfo(tvSeriesId)
    }

    fun getTvSeriesReviews(tvSeriesId: Int) {
        ApiClient.getTvSeriesReviews(tvSeriesId)
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