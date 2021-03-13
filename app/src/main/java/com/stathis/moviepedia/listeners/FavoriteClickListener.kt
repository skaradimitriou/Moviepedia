package com.stathis.moviepedia.listeners

import com.stathis.moviepedia.models.FavoriteMovies
import com.stathis.moviepedia.models.FavoriteTvSeries

interface FavoriteClickListener {

    fun onFavoriteMoviesClick(favoriteMovies:FavoriteMovies)
    fun onFavoriteTvSeriesClick(favoriteTvSeries:FavoriteTvSeries)
}