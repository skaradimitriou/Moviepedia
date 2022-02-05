package com.stathis.moviepedia.listeners.old

import com.stathis.moviepedia.models.FavoriteMovies
import com.stathis.moviepedia.models.FavoriteTvSeries

interface FavoriteClickListener {

    fun onFavoriteMoviesClick(favoriteMovies:FavoriteMovies)
    fun onFavoriteTvSeriesClick(favoriteTvSeries:FavoriteTvSeries)
}