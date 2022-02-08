package com.stathis.moviepedia.listeners.old

import com.stathis.moviepedia.models.genres.MovieGenres

interface GenresClickListener {

    fun onGenreClick(movieGenres: MovieGenres)
}