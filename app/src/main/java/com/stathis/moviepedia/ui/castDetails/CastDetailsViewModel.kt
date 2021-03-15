package com.stathis.moviepedia.ui.castDetails

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.stathis.moviepedia.listeners.ItemClickListener
import com.stathis.moviepedia.models.Movies
import com.stathis.moviepedia.models.TvSeries
import com.stathis.moviepedia.ui.castDetails.adapter.KnownMoviesAdapter

class CastDetailsViewModel : ViewModel(), ItemClickListener {

    private val repo = CastDetailsRepository()
    val actorData = repo.actorData
    val knownMovies = repo.knownMovies
    val adapter = KnownMoviesAdapter()

    fun getActorData(actorID: Int) {
        repo.getActorsData(actorID)
    }

    fun getActorsKnownMovies(actorID: Int) {
        repo.getActorsKnownMovies(actorID)
    }

    fun observeData(owner: LifecycleOwner) {
        knownMovies.observe(owner, Observer {
            Log.d("", it.toString())
            adapter.submitList(it.knownMovies)
        })
    }

    fun removeObservers(owner: LifecycleOwner) {
        knownMovies.removeObservers(owner)
    }

    override fun onItemClick(movies: Movies) {
        //
    }

    override fun onTvSeriesClick(tvSeries: TvSeries) {
       //
    }
}