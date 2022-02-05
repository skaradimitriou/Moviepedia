package com.stathis.moviepedia.listeners.old

import com.stathis.moviepedia.models.Movies
import com.stathis.moviepedia.models.TvSeries

interface ItemClickListener {
    fun onItemClick(movies: Movies)
    fun onTvSeriesClick(tvSeries: TvSeries)
}