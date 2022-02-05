package com.stathis.moviepedia.models.actor

import com.google.gson.annotations.SerializedName
import com.stathis.moviepedia.models.LocalModel

class KnownMovies(

    val character: String,

    @SerializedName("original_title")
    val title: String,

    @SerializedName("poster_path")
    val photo: String,

    val backdrop_path: String,
    val vote_average: Double
) : LocalModel{
    override fun equals(model: LocalModel): Boolean = false
}