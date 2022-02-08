package com.stathis.moviepedia.models.movies

import com.stathis.moviepedia.models.LocalModel

data class GenreMoviesFeed (
    val page:Int,
    val total_results:Int,
    val total_pages:Int,
    val results:List<Movies>
) : LocalModel {
    override fun equals(model: LocalModel): Boolean = false
}