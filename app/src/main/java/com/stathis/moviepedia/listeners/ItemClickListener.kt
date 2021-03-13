package com.stathis.moviepedia.listeners

import com.stathis.moviepedia.models.Movies
import com.stathis.moviepedia.models.TvSeries

interface ItemClickListener {
    fun onItemClick(movies: Movies)
    fun onTvSeriesClick(tvSeries: TvSeries)
}