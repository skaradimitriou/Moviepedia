package com.stathis.moviepedia.ui.dashboard.movies

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.stathis.moviepedia.adapters.*
import com.stathis.moviepedia.adapters.genres.GenresAdapter
import com.stathis.moviepedia.adapters.topRated.TopRatedAdapter
import com.stathis.moviepedia.adapters.upcoming.UpcomingAdapter
import com.stathis.moviepedia.listeners.old.GenresClickListener
import com.stathis.moviepedia.listeners.old.ItemClickListener
import com.stathis.moviepedia.models.*
import com.stathis.moviepedia.models.genres.MovieGenres
import com.stathis.moviepedia.models.movies.Movies
import com.stathis.moviepedia.models.series.TvSeries

class MoviesViewModel : ViewModel(), ItemClickListener, GenresClickListener {

    private val repo = MoviesRepository()
    private var upcomingMovies = repo.upcomingMovies
    private var trendingMovies = repo.trendingMovies
    private var movieGenres = repo.movieGenres
    private var topRatedMovies = repo.topRatedMovies
    private lateinit var emptyModelList: MutableList<EmptyModel>

    private lateinit var listener: ItemClickListener
    private lateinit var genresListener: GenresClickListener

    val upcomingAdapter = UpcomingAdapter(this)
    val trendingAdapter = TrendingAdapter(this)
    val topRatedAdapter = TopRatedAdapter(this)
    //val genresAdapter = GenresAdapter(this)

    init {
        //setShimmer()
    }

//    fun setShimmer(): List<EmptyModel> {
//        emptyModelList = listOf(
//            EmptyModel(""),
//            EmptyModel(""),
//            EmptyModel(""),
//            EmptyModel("")
//        )
//        return emptyModelList
//    }

    fun initListeners(listener: ItemClickListener, genresListener: GenresClickListener) {
        this.listener = listener
        this.genresListener = genresListener
    }

    fun observeData(owner: LifecycleOwner) {
        upcomingMovies.observe(owner, Observer { t ->
            upcomingAdapter.submitList(t)
            upcomingAdapter.notifyDataSetChanged()
        })

        trendingMovies.observe(owner, Observer { t ->
            trendingAdapter.submitList(t)
            trendingAdapter.notifyDataSetChanged()
        })

        topRatedMovies.observe(owner, Observer { t ->
            //sorting list by rating and passing it to the adapter
            topRatedAdapter.submitList(t.sortedWith(compareBy { it.vote_average }).reversed())
            topRatedAdapter.notifyDataSetChanged()
            Log.d("SortedList", t.sortedWith(compareBy { it.vote_average }).reversed().toString())
        })

        movieGenres.observe(owner, Observer { t ->
            //genresAdapter.submitList(t as List<Any>?)
            //genresAdapter.notifyDataSetChanged()
        })
    }

    fun removeObservers(owner: LifecycleOwner) {
        upcomingMovies.removeObservers(owner)
        trendingMovies.removeObservers(owner)
        topRatedMovies.removeObservers(owner)
        movieGenres.removeObservers(owner)
    }

    fun getUpcomingMovies() {
        //repo.getUpcomingMovies()
    }

    fun getTrendingMovies() {
        //repo.getTrendingMovies()
    }

    fun getMovieGenres() {
        //repo.getMovieGenres()
    }

    fun getTopRatedMovies() {
        //repo.getTopRatedMovies()
    }

    override fun onItemClick(movies: Movies) {
        listener.onItemClick(movies)
    }

    override fun onTvSeriesClick(tvSeries: TvSeries) {
        listener.onTvSeriesClick(tvSeries)
    }

    override fun onGenreClick(movieGenres: MovieGenres) {
        genresListener.onGenreClick(movieGenres)
    }
}