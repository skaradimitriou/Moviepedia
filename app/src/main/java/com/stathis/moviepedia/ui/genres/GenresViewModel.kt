package com.stathis.moviepedia.ui.genres

import android.view.View
import androidx.lifecycle.*
import com.stathis.moviepedia.listeners.GenericCallback
import com.stathis.moviepedia.listeners.MovieTypeCallback
import com.stathis.moviepedia.models.movies.Movies
import com.stathis.moviepedia.network.ApiClient
import com.stathis.moviepedia.ui.genres.adapter.MovieTypeAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class GenresViewModel : ViewModel(), GenericCallback {

    val data = MutableLiveData<List<Movies>>()
    val error = MutableLiveData<Boolean>()
    val adapter = MovieTypeAdapter(this)
    private lateinit var callback: MovieTypeCallback

    fun getResultsForThisGenre(genreId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            runBlocking {
                ApiClient.getResultsForThisGenre(genreId, data, error)
            }
        }
    }

    fun observe(owner: LifecycleOwner, callback: MovieTypeCallback) {
        this.callback = callback

        data.observe(owner, Observer {
            adapter.submitList(it)
        })
    }

    fun release(owner: LifecycleOwner) {
        data.removeObservers(owner)
    }

    override fun onItemTap(view: View) {
        when (view.tag) {
            is Movies -> callback.onMovieTap(view.tag as Movies)
        }
    }
}