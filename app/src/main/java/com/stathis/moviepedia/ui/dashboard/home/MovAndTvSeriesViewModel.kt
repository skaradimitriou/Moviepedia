package com.stathis.moviepedia.ui.dashboard.home

import android.view.View
import androidx.lifecycle.*
import com.stathis.moviepedia.adapters.*
import com.stathis.moviepedia.adapters.genres.GenresAdapter
import com.stathis.moviepedia.adapters.topRated.TopRatedAdapter
import com.stathis.moviepedia.adapters.upcoming.UpcomingAdapter
import com.stathis.moviepedia.listeners.GenericCallback
import com.stathis.moviepedia.listeners.HomeScreenCallback
import com.stathis.moviepedia.listeners.old.FavoriteClickListener
import com.stathis.moviepedia.listeners.old.GenresClickListener
import com.stathis.moviepedia.listeners.old.ItemClickListener
import com.stathis.moviepedia.models.*
import com.stathis.moviepedia.models.genres.MovieGenres
import com.stathis.moviepedia.models.movies.Movies
import com.stathis.moviepedia.models.movies.UpcomingMovies
import com.stathis.moviepedia.models.series.TvSeries
import com.stathis.moviepedia.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovAndTvSeriesViewModel : ViewModel(), ItemClickListener, GenresClickListener, FavoriteClickListener,GenericCallback {

    val upcomingMovies = MutableLiveData<List<Movies>>()
    val trendingMovies = MutableLiveData<List<Movies>>()
    val movieGenres = MutableLiveData<List<MovieGenres>>()
    val topRatedMovies = MutableLiveData<List<Movies>>()

    private lateinit var callback : HomeScreenCallback

    val upcomingAdapter = UpcomingAdapter(this)
    val trendingAdapter = TrendingAdapter(this)
    val topRatedAdapter = TopRatedAdapter(this)
    val genresAdapter = GenresAdapter(this)

    init {
        upcomingAdapter.submitList(setShimmer())
        trendingAdapter.submitList(setShimmer())
        topRatedAdapter.submitList(setShimmer())
        genresAdapter.submitList(setShimmer())

        viewModelScope.launch(Dispatchers.IO) {
            getUpcomingMovies()
            getTrendingMovies()
            getTopRatedMovies()
            getMovieGenres()
        }
    }

    private fun setShimmer() = listOf(
            EmptyModel(""),
            EmptyModel(""),
            EmptyModel(""),
            EmptyModel("")
    )

    fun observeData(owner: LifecycleOwner, callback : HomeScreenCallback) {
        this.callback = callback

        upcomingMovies.observe(owner, Observer {
            upcomingAdapter.submitList(it)
        })

        trendingMovies.observe(owner, Observer {
            trendingAdapter.submitList(it)
        })

        movieGenres.observe(owner, Observer {
            genresAdapter.submitList(it)
        })

        topRatedMovies.observe(owner, Observer {
            topRatedAdapter.submitList(it?.sortedWith(compareBy { it.vote_average })?.reversed())
        })
    }

    fun removeObservers(owner: LifecycleOwner) {
        upcomingMovies.removeObservers(owner)
        trendingMovies.removeObservers(owner)
        movieGenres.removeObservers(owner)
        topRatedMovies.removeObservers(owner)
    }

    private suspend fun getUpcomingMovies() {
        kotlin.runCatching {
            ApiClient.getCountries()
        }.onSuccess {
            when(it.isSuccessful){
                true -> upcomingMovies.postValue(it.body()?.results)
                false -> {}
            }
        }
    }

    private suspend fun getTrendingMovies() {
        kotlin.runCatching {
            ApiClient.getTrendingMovies()
        }.onSuccess {
            when(it.isSuccessful){
                true -> trendingMovies.postValue(it.body()?.results)
                false -> {}
            }
        }
    }

    private suspend fun getTopRatedMovies() {
        kotlin.runCatching {
            ApiClient.getTopRatedMovies()
        }.onSuccess {
            when(it.isSuccessful){
                true -> topRatedMovies.postValue(it.body()?.results)
                false -> {}
            }
        }
    }

    private suspend fun getMovieGenres() {
        kotlin.runCatching {
            ApiClient.getMovieGenres()
        }.onSuccess {
            when(it.isSuccessful){
                true -> movieGenres.postValue(it.body()?.genres)
                false -> {}
            }
        }
    }

    override fun onItemTap(view: View) = when(view.tag){
        is Movies -> callback.onMovieTap(view.tag as Movies)
        is UpcomingMovies -> callback.onUpcomingMovieTap(view.tag as UpcomingMovies)
        is MovieGenres -> callback.onGenreTap(view.tag as MovieGenres)
        else -> Unit
    }

    override fun onFavoriteMoviesClick(favoriteMovies: FavoriteMovies) {
    }

    override fun onFavoriteTvSeriesClick(favoriteTvSeries: FavoriteTvSeries) {
    }

    override fun onGenreClick(movieGenres: MovieGenres) {
    }

    override fun onItemClick(movies: Movies) {
    }

    override fun onTvSeriesClick(tvSeries: TvSeries) {
    }
}