package com.stathis.moviepedia.ui.dashboard.fragments.all

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.stathis.moviepedia.adapters.*
import com.stathis.moviepedia.models.*

class MovAndTvSeriesViewModel : ViewModel(), ItemClickListener, GenresClickListener,
    FavoriteClickListener {

    private val repo = MovAndTvRepository()
    private var upcomingMovies = repo.upcomingMovies
    private var popularMovies = repo.popularMovies
    private var movieGenres = repo.movieGenres
    private var topRatedMovies = repo.topRatedMovies
    private var favoriteMovies = repo.favoriteMovies

    private lateinit var listener: ItemClickListener
    private lateinit var genresListener: GenresClickListener
    private lateinit var favoriteListener: FavoriteClickListener

    val upcomingAdapter by lazy { UpcomingAdapter(this) }
    val trendingAdapter by lazy { TrendingAdapter(this) }
    val topRatedAdapter by lazy { TopRatedAdapter(this) }
    val genresAdapter by lazy { GenresAdapter(this) }

    init {
        setShimmer()
    }

    fun setShimmer(): MutableList<EmptyModel> {
        return mutableListOf(
            EmptyModel(""),
            EmptyModel(""),
            EmptyModel(""),
            EmptyModel("")
        )
    }

    fun initCallbacks(
        listener: ItemClickListener,
        genresListener: GenresClickListener,
        favoriteListener: FavoriteClickListener
    ) {
        this.listener = listener
        this.genresListener = genresListener
        this.favoriteListener = favoriteListener
    }

    fun observeData(owner: LifecycleOwner) {
        upcomingMovies.observe(owner, Observer {
            upcomingAdapter.submitList(it as List<Any>?)
            upcomingAdapter.notifyDataSetChanged()
        })

        popularMovies.observe(owner, Observer {
            trendingAdapter.submitList(it as List<Any>?)
            trendingAdapter.notifyDataSetChanged()
        })

        movieGenres.observe(owner, Observer {
            genresAdapter.submitList(it as List<Any>?)
            genresAdapter.notifyDataSetChanged()
        })

        topRatedMovies.observe(owner, Observer {
            topRatedAdapter.submitList(it?.sortedWith(compareBy { it.vote_average })?.reversed())
            topRatedAdapter.notifyDataSetChanged()
        })

        favoriteMovies.observe(owner, Observer {

        })
    }

    fun removeObservers(owner: LifecycleOwner) {
        upcomingMovies.removeObservers(owner)
        popularMovies.removeObservers(owner)
        movieGenres.removeObservers(owner)
        topRatedMovies.removeObservers(owner)
        favoriteMovies.removeObservers(owner)
    }

    fun getUpcomingMovies() {
        repo.getUpcomingMovies()
    }

    fun getTrendingMovies() {
        repo.getTrendingMovies()
    }

    fun getTopRatedMovies() {
        repo.getTopRatedMovies()
    }

    fun getFavoriteMovies() {
        repo.getFavoriteMovies()
    }

    fun getMovieGenres() {
        repo.getMovieGenres()
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

    override fun onFavoriteMoviesClick(favoriteMovies: FavoriteMovies) {
        favoriteListener.onFavoriteMoviesClick(favoriteMovies)
    }

    override fun onFavoriteTvSeriesClick(favoriteTvSeries: FavoriteTvSeries) {
        favoriteListener.onFavoriteTvSeriesClick(favoriteTvSeries)
    }
}