package com.stathis.moviepedia.listeners.old

import com.stathis.moviepedia.models.movies.Movies
import com.stathis.moviepedia.models.series.TvSeries

interface ItemClickListener {
    fun onItemClick(movies: Movies)
    fun onTvSeriesClick(tvSeries: TvSeries)
}