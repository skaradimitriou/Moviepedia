package com.stathis.moviepedia.ui.movieInfoScreen

import android.view.View
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.stathis.moviepedia.adapters.CastAdapter
import com.stathis.moviepedia.adapters.ReviewsAdapter
import com.stathis.moviepedia.listeners.CastCallback
import com.stathis.moviepedia.listeners.GenericCallback
import com.stathis.moviepedia.models.*
import com.stathis.moviepedia.models.cast.Cast
import com.stathis.moviepedia.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MovieDetailsViewModel : ViewModel(), GenericCallback {

    val castInfo = MutableLiveData<List<Cast>>()
    val castDataError = MutableLiveData<Boolean>()
    val reviews = MutableLiveData<List<Reviews>>()
    val reviewsError = MutableLiveData<Boolean>()
    val isFavorite = MutableLiveData<Boolean>()
    val adapter = CastAdapter(this)
    val reviewsAdapter = ReviewsAdapter()

    private lateinit var callback: CastCallback
    val databaseReference by lazy { FirebaseDatabase.getInstance().reference }
    val auth by lazy { FirebaseAuth.getInstance() }

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

    fun getAdditionalDetails(movieId: Int, movieTitle: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runBlocking {
                ApiClient.getMovieCastInfo(movieId, castInfo, castDataError)
                ApiClient.getMovieReviews(movieId, reviews, reviewsError)
                getFavoritesFromDb(movieTitle)
            }
        }
    }

    fun observeData(owner: LifecycleOwner, callback: CastCallback) {
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
        databaseReference.child("users")
            .child(auth.currentUser?.uid.toString())
            .child("favoriteMovieList")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        p0.children.forEach {
                            val fav = it.getValue(FavoriteMovies::class.java)
                            if (fav?.title == movieTitle) {
                                isFavorite.postValue( true)
                            }
                        }
                    }
                }
            })
    }

    fun removeFromFavorites(movieTitle: String) {
        databaseReference.child("users")
            .child(auth.currentUser?.uid.toString())
            .child("favoriteMovieList")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}

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
        databaseReference.child("users")
            .child(auth.currentUser?.uid.toString())
            .child("favoriteMovieList")
            .child(movie.title).setValue(movie)
        isFavorite.value = true
    }

    override fun onItemTap(view: View) = when(view.tag){
        is Cast -> callback.onCastClick(view.tag as Cast)
        else -> Unit
    }
}