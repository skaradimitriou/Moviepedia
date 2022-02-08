package com.stathis.moviepedia.models.movies

import com.stathis.moviepedia.models.LocalModel

data class UpcomingMovies (
    val results:List<Movies>
) : LocalModel {
    override fun equals(model: LocalModel): Boolean = false
}