package com.stathis.moviepedia.models.genres

import com.stathis.moviepedia.models.LocalModel

class MovieGenres(
    val id: Int,
    val name: String
) : LocalModel {
    constructor(): this(0,"")
    override fun equals(model: LocalModel): Boolean = false
}