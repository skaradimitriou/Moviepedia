package com.stathis.moviepedia.ui.movieInfoScreen

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.stathis.moviepedia.adapters.CastAdapter
import com.stathis.moviepedia.adapters.ReviewsAdapter
import com.stathis.moviepedia.listeners.old.LocalClickListener
import com.stathis.moviepedia.models.*
import com.stathis.moviepedia.models.cast.Cast

class MovieInfoScreenViewModel : ViewModel(), LocalClickListener {

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