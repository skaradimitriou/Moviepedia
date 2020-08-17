package com.stathis.moviepedia.recyclerviews

import com.stathis.moviepedia.models.search.Query
import com.stathis.moviepedia.models.search.SearchItem

interface SearchItemClickListener {
    fun onQueryClick(query: Query)
    fun onSearchItemClick(searchItem: SearchItem)
}