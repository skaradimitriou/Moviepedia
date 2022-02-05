package com.stathis.moviepedia.models

class FavoriteTvSeries(
    val id: Int,
    val photo: String,
    val title: String,
    val movie_rating: Double,
    val description: String,
    val releaseDate: String
) : LocalModel {
    override fun equals(model: LocalModel): Boolean = false
    constructor() : this(0, "", "", 0.0, "", "")
}