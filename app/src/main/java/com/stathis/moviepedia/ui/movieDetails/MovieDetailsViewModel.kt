package com.stathis.moviepedia.ui.movieDetails

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.stathis.moviepedia.ui.movieDetails.adapter.CastAdapter
import com.stathis.moviepedia.ui.movieDetails.adapter.ReviewsAdapter
import com.stathis.moviepedia.listeners.CastCallback
import com.stathis.moviepedia.listeners.GenericCallback
import com.stathis.moviepedia.models.*
import com.stathis.moviepedia.models.cast.Cast
import com.stathis.moviepedia.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieDetailsViewModel : ViewModel(), GenericCallback {

    val castInfo = MutableLiveData<List<Cast>>()
    val castDataError = MutableLiveData<Boolean>()
    val reviews = MutableLiveData<List<Reviews>>()
    val reviewsError = MutableLiveData<Boolean>()
    val isFavorite = MutableLiveData<Boolean>()
    val adapter = CastAdapter(this)
    val reviewsAdapter = ReviewsAdapter()
    private val dbRef by lazy { FirebaseDatabase.getInstance().reference }
    val auth by lazy { FirebaseAuth.getInstance() }

    private lateinit var callback: CastCallback

    init {
        adapter.submitList(
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

    fun getAdditionalDetails(movieId: Int,movieTitle: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getCast(movieId)
            getReviews(movieId)
            getFavoritesFromDb(movieTitle)
        }
    }

    suspend fun getCast(movieId : Int){
        kotlin.runCatching {
            ApiClient.getMovieCastInfo(movieId)
        }.onSuccess {
            Log.d("TAG","Got Cast Response")
            when(it.isSuccessful){
                true -> castInfo.postValue(it.body()?.cast)
                false -> castDataError.postValue(false)
            }
        }.onFailure {
            castDataError.postValue(true)
        }
    }

    suspend fun getReviews(movieId: Int){
        kotlin.runCatching {
            ApiClient.getMovieReviews(movieId)
        }.onSuccess {
            Log.d("TAG","Got Reviews Response")
            when(it.isSuccessful) {
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
            adapter.submitList(it)
        })

        reviews.observe(owner, Observer {
            reviewsAdapter.submitList(it)
        })
    }

    fun removeObservers(owner: LifecycleOwner) {
        castInfo.removeObservers(owner)
        reviews.removeObservers(owner)
    }

    fun getFavoritesFromDb(movieTitle: String) {
        dbRef.child("users")
            .child(auth.currentUser?.uid.toString())
            .child("favoriteMovieList")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        p0.children.forEach {
                            val fav = it.getValue(Movies::class.java)
                            if (fav?.title == movieTitle) {
                                isFavorite.postValue(true)
                            }
                        }
                    }
                }
            })
    }

    fun removeFromFavorites(movieTitle: String) {
        dbRef.child("users")
            .child(auth.currentUser?.uid.toString())
            .child("favoriteMovieList")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        p0.children.forEach {
                            val favorite = it.getValue(Movies::class.java)
                            if (favorite?.title == movieTitle) {
                                it.ref.removeValue()
                                isFavorite.value = false
                            }
                        }
                    }
                }
            })
    }

    fun addToFavorites(movie: Movies) {
        dbRef.child("users")
            .child(auth.currentUser?.uid.toString())
            .child("favoriteMovieList")
            .child(movie.title).setValue(movie)
        isFavorite.value = true
    }

    override fun onItemTap(view: View) = when (view.tag) {
        is Cast -> callback.onCastClick(view.tag as Cast)
        else -> Unit
    }
}