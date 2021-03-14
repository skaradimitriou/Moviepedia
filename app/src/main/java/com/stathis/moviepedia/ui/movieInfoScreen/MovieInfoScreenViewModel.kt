package com.stathis.moviepedia.ui.movieInfoScreen

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.R
import com.stathis.moviepedia.adapters.CastAdapter
import com.stathis.moviepedia.adapters.ReviewsAdapter
import com.stathis.moviepedia.listeners.LocalClickListener
import com.stathis.moviepedia.models.*
import com.stathis.moviepedia.models.cast.Cast
import com.stathis.moviepedia.models.cast.MovieCastFeed
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class MovieInfoScreenViewModel : ViewModel(),LocalClickListener {

    private val repo = MoviesInfoRepository()
    private val castInfo = repo.castInfo
    private val reviews = repo.reviews
    val isFavorite = repo.isFavorite
    val adapter = CastAdapter(this)
    val reviewsAdapter = ReviewsAdapter()
    private lateinit var callback : LocalClickListener

    init {
        adapter.submitList(
            mutableListOf(
                EmptyModel(""),
                EmptyModel(""),
                EmptyModel(""),
                EmptyModel(""),
                EmptyModel(""),
                EmptyModel(""),
                EmptyModel("")
            ) as List<LocalModel>?
        )
    }

    fun getMovieCastInfo(movieId: Int) {
        repo.getMovieCastInfo(movieId)
    }

    fun getMovieReviews(movieId: Int) {
        repo.getMovieReviews(movieId)
    }

    fun initListener(callback : LocalClickListener){
        this.callback = callback
    }

    fun observeData(owner: LifecycleOwner) {
        castInfo.observe(owner, Observer { cast ->
            Log.d("CAST", cast.toString())
            adapter.submitList(cast)
            adapter.notifyDataSetChanged()
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
        repo.getFavoritesFromDb(movieTitle)
    }

    fun addToFavorites(movie: FavoriteMovies) {
        repo.addToFavorites(movie)
    }

    fun removeFromFavorites(movieTitle: String) {
        repo.removeFromFavorites(movieTitle)
    }

    override fun onCastClick(cast: Cast) {
        callback.onCastClick(cast)
    }
}