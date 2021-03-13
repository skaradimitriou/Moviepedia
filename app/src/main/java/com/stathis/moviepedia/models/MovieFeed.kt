package com.stathis.moviepedia.models

data class MovieFeed (
    val page:Int,
    val results:List<Movies>
) : LocalModel