package com.stathis.moviepedia.ui.dashboard.fragments.tvSeries

import android.util.Log
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.adapters.*
import com.stathis.moviepedia.models.*
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class TvSeriesViewModel : ViewModel(), ItemClickListener, GenresClickListener {

    private val repo = TvSeriesRepository()
    private lateinit var callback : ItemClickListener
    private lateinit var listener : GenresClickListener

    val upcomingAdapter by lazy { UpcomingAdapter(this) }
    val trendingAdapter by lazy { TrendingAdapter(this) }
    val topRatedAdapter by lazy { TopRatedAdapter(this) }
    val genresAdapter by lazy { GenresAdapter(this) }
    val airingAdapter by lazy { AiringTvSeriesAdapter(this) }

    val featuredTvSeries = repo.featuredTvSeries
    val airingToday = repo.airingToday
    val topRated = repo.topRated
    val popularTvSeries = repo.popularTvSeries
    val tvSeriesGenres = repo.tvSeriesGenres
    val emptyModelList: MutableList<EmptyModel> = mutableListOf()

    init {
        setShimmer()
    }

    fun initListener(callback : ItemClickListener,listener : GenresClickListener) {
        this.callback = callback
        this.listener = listener
    }

    fun setShimmer(): MutableList<EmptyModel> {
        val emptyModel = EmptyModel("")
        emptyModelList.add(emptyModel)
        emptyModelList.add(emptyModel)
        emptyModelList.add(emptyModel)
        emptyModelList.add(emptyModel)
        return emptyModelList
    }

    fun getFeaturedTvSeries() {
        repo.getFeaturedTvSeries()
    }

    fun getAiringTodayTvSeries() {
        repo.getAiringTodayTvSeries()
    }

    fun getTopRatedTvSeries() {
        repo.getTopRatedTvSeries()
    }

    fun getPopularTvSeries() {
        repo.getPopularTvSeries()
    }

    fun getTvGenres() {
        repo.getTvGenres()
    }

    fun observeData(owner: LifecycleOwner) {
        featuredTvSeries.observe(owner, Observer { t ->
            Log.d("Featured TvSeries", t.toString())
            upcomingAdapter.submitList(t as List<Any>?)
            upcomingAdapter.notifyDataSetChanged()
        })

        airingToday.observe(owner, Observer<MutableList<TvSeries>> { t ->
            trendingAdapter.submitList(t as List<Any>?)
            trendingAdapter.notifyDataSetChanged()
        })

        topRated.observe(owner, Observer<MutableList<TvSeries>> { t ->
            topRatedAdapter.submitList(t as List<Any>?)
            topRatedAdapter.notifyDataSetChanged()
        })

        popularTvSeries.observe(owner, Observer<MutableList<TvSeries>> { t ->
            airingAdapter.submitList(t as List<Any>?)
            airingAdapter.notifyDataSetChanged()
        })

        tvSeriesGenres.observe(owner, Observer<MutableList<MovieGenres>> { t ->
            genresAdapter.submitList(t as List<Any>?)
            genresAdapter.notifyDataSetChanged()
        })
    }

    fun removeObservers(owner: LifecycleOwner) {
        featuredTvSeries.removeObservers(owner)
        airingToday.removeObservers(owner)
        topRated.removeObservers(owner)
        popularTvSeries.removeObservers(owner)
        tvSeriesGenres.removeObservers(owner)
    }

    override fun onItemClick(movies: Movies) {
        callback.onItemClick(movies)
    }

    override fun onTvSeriesClick(tvSeries: TvSeries) {
        callback.onTvSeriesClick(tvSeries)
    }

    override fun onGenreClick(movieGenres: MovieGenres) {
        listener.onGenreClick(movieGenres)
    }
}