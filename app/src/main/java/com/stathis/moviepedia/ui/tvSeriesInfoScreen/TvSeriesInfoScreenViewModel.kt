package com.stathis.moviepedia.ui.tvSeriesInfoScreen

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.stathis.moviepedia.adapters.CastAdapter
import com.stathis.moviepedia.adapters.ReviewsAdapter
import com.stathis.moviepedia.models.EmptyModel
import com.stathis.moviepedia.models.FavoriteTvSeries

class TvSeriesInfoScreenViewModel : ViewModel() {

    private val repo by lazy { TvSeriesInfoRepository() }
    val castAdapter by lazy { CastAdapter() }
    val reviewsAdapter by lazy { ReviewsAdapter() }
    private var cast = repo.cast
    private var reviews = repo.reviews
    val isFavorite = repo.isFavorite

    init {
        castAdapter.submitList(
            mutableListOf(
                EmptyModel(""),
                EmptyModel(""),
                EmptyModel(""),
                EmptyModel(""),
                EmptyModel(""),
                EmptyModel(""),
                EmptyModel("")
            ) as List<Any>?
        )
    }

    fun observeDataFromApi(owner: LifecycleOwner) {
        cast.observe(owner, Observer { cast ->
            Log.d("cast is:", cast.toString())
            castAdapter.submitList(cast as List<Any>?)
            castAdapter.notifyDataSetChanged()
        })

        reviews.observe(owner, Observer { reviews ->
            Log.d("reviews:", reviews.toString())
            reviewsAdapter.submitList(reviews as List<Any>?)
        })
    }

    fun removeObservers(owner: LifecycleOwner) {
        cast.removeObservers(owner)
        reviews.removeObservers(owner)
    }

    fun getCastInfo(tvSeriesId: Int) {
        repo.getCastInfo(tvSeriesId)
    }

    fun getTvSeriesReviews(tvSeriesId: Int) {
        repo.getTvSeriesReviews(tvSeriesId)
    }

    fun addToFavorites(favorite : FavoriteTvSeries) {
        repo.addToFavorites(favorite)
    }

    fun removeFromFavorites(tvSeriesId: Int) {
        repo.removeFromFavorites(tvSeriesId)
    }

    fun getUserFavorites(tvSeriesId: Int) {
        repo.getUserFavorites(tvSeriesId)
    }
}