package com.stathis.moviepedia.ui.genresInfoScreen

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.stathis.moviepedia.listeners.ItemClickListener
import com.stathis.moviepedia.models.EmptyModel
import com.stathis.moviepedia.models.Movies
import com.stathis.moviepedia.models.TvSeries
import com.stathis.moviepedia.ui.genresInfoScreen.adapters.MoviesAdapter
import com.stathis.moviepedia.util.ColorHelper

class GenresInfoScreenViewModel(application: Application) : AndroidViewModel(application),
    ItemClickListener {

    private val repo = GenresRepository()
    val movies = repo.movies
    val headerColor = ColorHelper.color
    val movie = MutableLiveData<Movies>()
    val adapter = MoviesAdapter(this)

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
            ) as List<Any>?
        )
    }

    fun getResultsForThisGenre(genreId: Int) {
        repo.getResultsForThisGenre(genreId)
    }

    fun observeData(owner: LifecycleOwner) {
        movies.observe(owner, Observer { movies ->
            Log.d("RESULT", movies.toString())
            adapter.submitList(movies as List<Any>?)
            adapter.notifyDataSetChanged()
        })
    }

    fun removeObservers(owner: LifecycleOwner) {
        headerColor.removeObservers(owner)
        movie.removeObservers(owner)
        movies.removeObservers(owner)
    }

    fun getBackgroundColor(genreName: String) {
        ColorHelper.getBackgroundColor(genreName)
    }

    override fun onItemClick(movies: Movies) {
        movie.value = movies
    }

    override fun onTvSeriesClick(tvSeries: TvSeries) {}
}