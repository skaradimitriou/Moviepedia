package com.stathis.moviepedia.ui.dashboard.fragments.search

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.adapters.SearchAdapter
import com.stathis.moviepedia.adapters.SearchItemClickListener
import com.stathis.moviepedia.models.EmptyModel
import com.stathis.moviepedia.ui.dashboard.fragments.search.models.Query
import com.stathis.moviepedia.ui.dashboard.fragments.search.models.SearchItem
import com.stathis.moviepedia.ui.dashboard.fragments.search.models.SearchItemsFeed
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

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
            ) as List<Any>?
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
            searchAdapter.submitList(queries as List<Any>?)
            searchAdapter.notifyDataSetChanged()
        })

        recentSearches.observe(owner, Observer { searchItem ->
            Log.d("Search Item", searchItem.toString())
            searchAdapter.submitList(searchItem as List<Any>?)
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