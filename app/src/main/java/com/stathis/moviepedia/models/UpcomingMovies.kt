package com.stathis.moviepedia.models

data class UpcomingMovies (
    val results:List<Movies>
) : LocalModel{
    override fun equals(model: LocalModel): Boolean = false
}