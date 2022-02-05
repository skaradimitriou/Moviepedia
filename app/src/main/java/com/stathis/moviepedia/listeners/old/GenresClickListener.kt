package com.stathis.moviepedia.listeners.old

import com.stathis.moviepedia.models.MovieGenres

interface GenresClickListener {

    fun onGenreClick(movieGenres: MovieGenres)
}