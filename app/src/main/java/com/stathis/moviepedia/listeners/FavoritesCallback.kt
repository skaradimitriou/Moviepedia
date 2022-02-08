package com.stathis.moviepedia.listeners

import com.stathis.moviepedia.models.movies.Movies
import com.stathis.moviepedia.models.series.TvSeries

interface FavoritesCallback {
    fun onMovieTap(model : Movies)
    fun onTvSeriesTap(model : TvSeries)
}