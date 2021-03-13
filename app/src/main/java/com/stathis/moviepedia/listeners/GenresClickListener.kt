package com.stathis.moviepedia.listeners

import com.stathis.moviepedia.models.MovieGenres

interface GenresClickListener {

    fun onGenreClick(movieGenres: MovieGenres)
}