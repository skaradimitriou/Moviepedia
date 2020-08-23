package com.stathis.moviepedia.models.search

import com.stathis.moviepedia.models.Movies

class SearchItemsFeed (
    val page:Int,
    val total_results:Int,
    val total_pages:Int,
    val results:MutableList<SearchItem>
)