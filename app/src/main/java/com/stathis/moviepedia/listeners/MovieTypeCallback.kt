package com.stathis.moviepedia.listeners

import com.stathis.moviepedia.models.Movies

interface MovieTypeCallback {
    fun onMovieTap(movie : Movies)
}