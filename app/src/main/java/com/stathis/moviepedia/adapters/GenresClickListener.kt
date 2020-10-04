package com.stathis.moviepedia.adapters

import com.stathis.moviepedia.models.MovieGenres

interface GenresClickListener {

    fun onGenreClick(movieGenres: MovieGenres)
}