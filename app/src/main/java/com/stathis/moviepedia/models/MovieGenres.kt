package com.stathis.moviepedia.models

class MovieGenres(
    val id: Int,
    val name: String
) : LocalModel {
    constructor(): this(0,"")
    override fun equals(model: LocalModel): Boolean = false
}