package com.stathis.moviepedia.models.search

import com.stathis.moviepedia.models.Movies
import com.stathis.moviepedia.models.TvSeries

class SearchItemsFeed (
    val page:Int,
    val total_results:Int,
    val total_pages:Int,
    val results:MutableList<Movies>
)