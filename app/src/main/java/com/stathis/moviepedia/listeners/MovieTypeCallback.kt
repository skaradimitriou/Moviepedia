package com.stathis.moviepedia.listeners

import com.stathis.moviepedia.models.movies.Movies

interface MovieTypeCallback {
    fun onMovieTap(movie : Movies)
}