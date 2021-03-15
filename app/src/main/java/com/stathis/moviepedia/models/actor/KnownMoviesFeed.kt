package com.stathis.moviepedia.models.actor

import com.google.gson.annotations.SerializedName
import com.stathis.moviepedia.models.LocalModel

class KnownMoviesFeed(

    @SerializedName("cast")
    val knownMovies: List<KnownMovies>
) : LocalModel