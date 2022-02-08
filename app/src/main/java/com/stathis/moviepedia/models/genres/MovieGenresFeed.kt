package com.stathis.moviepedia.models.genres

import com.stathis.moviepedia.models.LocalModel

data class MovieGenresFeed (
    val genres:List<MovieGenres>
) : LocalModel {
    override fun equals(model: LocalModel): Boolean = false
}