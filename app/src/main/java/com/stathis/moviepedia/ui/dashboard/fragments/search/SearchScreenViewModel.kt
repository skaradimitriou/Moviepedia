package com.stathis.moviepedia.ui.dashboard.fragments.search

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.stathis.moviepedia.adapters.SearchAdapter
import com.stathis.moviepedia.listeners.SearchItemClickListener
import com.stathis.moviepedia.models.EmptyModel
import com.stathis.moviepedia.models.LocalModel
import com.stathis.moviepedia.ui.dashboard.fragments.search.models.Query
import com.stathis.moviepedia.ui.dashboard.fragments.search.models.SearchItem

class SearchScreenViewModel : ViewModel(), SearchItemClickListener {

    private val repo = SearchRepository()
    val searchAdapter = SearchAdapter(this)
    val recentSearches = repo.recentSearches
    val queries = repo.queries
    private lateinit var callback: SearchItemClickListener

    init {
        searchAdapter.submitList(
            mutableListOf(
                EmptyModel(""),
                EmptyModel(""),
                EmptyModel(""),
                EmptyModel(""),
                EmptyModel(""),
                EmptyModel(""),
                EmptyModel("")
            ) as List<LocalModel>?
        )
    }

    fun initListener(callback: SearchItemClickListener) {
        this.callback = callback
    }

    fun getQueryInfo(query: Query) {
        //saving the user queries to the db so we can access them for an enhanced User Experience
        if (query.queryName != "null") {
            repo.addQueryToDb(query)
        }

        //Building the url by replacing spacings with "+" so I can call the API for results
        if (query.queryName.contains(" ")) {
            query.queryName = query.queryName.replace("\\s".toRegex(), "+")
        }

        repo.getQueryInfo(query)
    }

    fun getRecentUserQueries() {
        repo.getRecentUserQueries()
    }

    fun observeData(owner: LifecycleOwner) {
        queries.observe(owner, Observer { queries ->
            Log.d("Queries", queries.toString())
            searchAdapter.submitList(queries as List<LocalModel>?)
            searchAdapter.notifyDataSetChanged()
        })

        recentSearches.observe(owner, Observer { searchItem ->
            Log.d("Search Item", searchItem.toString())
            searchAdapter.submitList(searchItem as List<LocalModel>?)
            searchAdapter.notifyDataSetChanged()
        })
    }

    fun removeObservers(owner: LifecycleOwner) {
        queries.removeObservers(owner)
        recentSearches.removeObservers(owner)
    }

    override fun onQueryClick(query: Query) {
        callback.onQueryClick(query)
    }

    override fun onSearchItemClick(searchItem: SearchItem) {
        callback.onSearchItemClick(searchItem)
    }
}