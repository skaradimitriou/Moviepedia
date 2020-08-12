package com.stathis.moviepedia.recyclerviews

import com.stathis.moviepedia.models.MovieGenres

interface GenresClickListener {

    fun onGenreClick(movieGenres: MovieGenres)
}