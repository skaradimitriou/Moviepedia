package com.stathis.moviepedia.ui.tvSeriesInfoScreen

import android.util.Log
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.stathis.moviepedia.adapters.CastAdapter
import com.stathis.moviepedia.adapters.ReviewsAdapter
import com.stathis.moviepedia.listeners.CastCallback
import com.stathis.moviepedia.listeners.GenericCallback
import com.stathis.moviepedia.models.EmptyModel
import com.stathis.moviepedia.models.FavoriteTvSeries
import com.stathis.moviepedia.models.LocalModel
import com.stathis.moviepedia.models.cast.Cast

class TvSeriesInfoScreenViewModel : ViewModel(), CastCallback {

    private val repo by lazy { TvSeriesInfoRepository() }
    val castAdapter by lazy { CastAdapter(object : GenericCallback{
        override fun onItemTap(view: View) {
            //FIXME: Remove this when you refactor this screen
        }
    }) }
    val reviewsAdapter by lazy { ReviewsAdapter() }
    private var cast = repo.cast
    private var reviews = repo.reviews
    val isFavorite = repo.isFavorite
    private lateinit var callback: CastCallback

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
            ) as List<LocalModel>?
        )
    }

    fun observeDataFromApi(owner: LifecycleOwner,callback: CastCallback) {
        this.callback = callback
        cast.observe(owner, Observer { cast ->
            Log.d("cast is:", cast.toString())
            castAdapter.submitList(cast)
            castAdapter.notifyDataSetChanged()
        })

        reviews.observe(owner, Observer { reviews ->
            Log.d("reviews:", reviews.toString())
            reviewsAdapter.submitList(reviews)
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

    fun addToFavorites(favorite: FavoriteTvSeries) {
        repo.addToFavorites(favorite)
    }

    fun removeFromFavorites(tvSeriesId: Int) {
        repo.removeFromFavorites(tvSeriesId)
    }

    fun getUserFavorites(tvSeriesId: Int) {
        repo.getUserFavorites(tvSeriesId)
    }

    override fun onCastClick(cast: Cast) {
        callback.onCastClick(cast)
    }
}