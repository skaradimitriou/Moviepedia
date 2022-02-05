package com.stathis.moviepedia.listeners.old

import com.stathis.moviepedia.ui.dashboard.fragments.search.models.Query
import com.stathis.moviepedia.ui.dashboard.fragments.search.models.SearchItem

interface SearchItemClickListener {
    fun onQueryClick(query: Query)
    fun onSearchItemClick(searchItem: SearchItem)
}