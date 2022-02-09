package com.stathis.moviepedia.listeners

import com.stathis.moviepedia.models.genres.MovieGenres
import com.stathis.moviepedia.models.movies.Movies
import com.stathis.moviepedia.models.movies.UpcomingMovies
import com.stathis.moviepedia.models.series.TvSeries

interface HomeScreenCallback {
    fun onUpcomingMovieTap(model: UpcomingMovies)
    fun onMovieTap(model : Movies)
    fun onGenreTap(model: MovieGenres)
}