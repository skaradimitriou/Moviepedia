package com.stathis.moviepedia.models

data class MovieGenresFeed (
    val genres:List<MovieGenres>
) : LocalModel{
    override fun equals(model: LocalModel): Boolean = false
}