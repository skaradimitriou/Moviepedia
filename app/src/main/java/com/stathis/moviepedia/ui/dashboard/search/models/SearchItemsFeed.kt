package com.stathis.moviepedia.ui.dashboard.search.models

class SearchItemsFeed (
    val page:Int,
    val total_results:Int,
    val total_pages:Int,
    val results:MutableList<SearchItem>
)