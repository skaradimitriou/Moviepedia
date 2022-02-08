package com.stathis.moviepedia.ui.tvSeriesDetails

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.stathis.moviepedia.ui.movieDetails.adapter.CastAdapter
import com.stathis.moviepedia.ui.movieDetails.adapter.ReviewsAdapter
import com.stathis.moviepedia.listeners.CastCallback
import com.stathis.moviepedia.listeners.GenericCallback
import com.stathis.moviepedia.models.EmptyModel
import com.stathis.moviepedia.models.FavoriteTvSeries
import com.stathis.moviepedia.models.reviews.Reviews
import com.stathis.moviepedia.models.series.TvSeries
import com.stathis.moviepedia.models.cast.Cast
import com.stathis.moviepedia.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TvSeriesDetailsViewModel : ViewModel(), GenericCallback {

    private val dbRef by lazy { FirebaseDatabase.getInstance().reference }
    private val auth by lazy { FirebaseAuth.getInstance() }
    val castAdapter = CastAdapter(this)
    val reviewsAdapter by lazy { ReviewsAdapter() }
    val reviewsError = MutableLiveData<Boolean>()
    val castInfo = MutableLiveData<List<Cast>>()
    val castDataError = MutableLiveData<Boolean>()
    val reviews = MutableLiveData<List<Reviews>>()
    val isFavorite = MutableLiveData<Boolean>()
    private lateinit var callback: CastCallback

    init {
        castAdapter.submitList(
            listOf(
                EmptyModel(""),
                EmptyModel(""),
                EmptyModel(""),
                EmptyModel(""),
                EmptyModel(""),
                EmptyModel(""),
                EmptyModel("")
            )
        )
    }

    fun getTvSeriesInfo(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            getCast(id)
            getReviews(id)
            getFavoritesFromDb(id)
        }
    }

    suspend fun getCast(movieId: Int) {
        kotlin.runCatching {
            ApiClient.getTvCastInfo(movieId)
        }.onSuccess {
            Log.d("TAG", "Got Cast Response")
            when (it.isSuccessful) {
                true -> castInfo.postValue(it.body()?.cast)
                false -> castDataError.postValue(false)
            }
        }.onFailure {
            castDataError.postValue(true)
        }
    }

    suspend fun getReviews(movieId: Int) {
        kotlin.runCatching {
            ApiClient.getTvReviews(movieId)
        }.onSuccess {
            Log.d("TAG", "Got Reviews Response")
            when (it.isSuccessful) {
                true -> reviews.postValue(it.body()?.results)
                false -> reviewsError.postValue(false)
            }
        }.onFailure {
            reviewsError.postValue(true)
        }
    }

    fun observe(owner: LifecycleOwner, callback: CastCallback) {
        this.callback = callback

        castInfo.observe(owner, Observer {
            castAdapter.submitList(it)
        })

        reviews.observe(owner, Observer {
            reviewsAdapter.submitList(it)
        })
    }

    fun removeObservers(owner: LifecycleOwner) {
        castInfo.removeObservers(owner)
        reviews.removeObservers(owner)
    }

    fun addToFavorites(tvSeries: TvSeries) {
        dbRef.child("users")
            .child(auth.currentUser?.uid.toString())
            .child("favoriteTvSeries")
            .child(tvSeries.id.toString()).setValue(tvSeries)
        isFavorite.value = true
    }

    fun getFavoritesFromDb(tvSeriesId: Int) {
        dbRef.child("users")
            .child(auth.currentUser?.uid.toString())
            .child("favoriteTvSeries")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}
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
        dbRef.child("users")
            .child(auth.currentUser?.uid.toString())
            .child("favoriteTvSeries")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}
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

    override fun onItemTap(view: View) = when (view.tag) {
        is Cast -> callback.onCastClick(view.tag as Cast)
        else -> Unit
    }
}