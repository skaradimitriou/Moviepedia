package com.stathis.moviepedia.ui.dashboard.fragments.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.models.EmptyModel
import com.stathis.moviepedia.ui.dashboard.fragments.search.models.Query
import com.stathis.moviepedia.ui.dashboard.fragments.search.models.SearchItem
import com.stathis.moviepedia.ui.dashboard.fragments.search.models.SearchItemsFeed
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class SearchScreenViewModel : ViewModel() {

    private lateinit var url: String
    private lateinit var request: Request
    private lateinit var client: OkHttpClient
    private var apiKey: String = "b36812048cc4b54d559f16a2ff196bc5"
    private var searchItems: MutableList<SearchItem> = mutableListOf()
    private var userQueries: MutableList<Query> = mutableListOf()
    private lateinit var databaseReference: DatabaseReference
    private var recentSearches: MutableLiveData<MutableList<SearchItem>> = MutableLiveData()
    private var queries: MutableLiveData<MutableList<Query>> = MutableLiveData()
    private lateinit var emptyModelList: MutableList<EmptyModel>

    init {
        setShimmer()
    }

    fun setShimmer(): MutableList<EmptyModel> {
        emptyModelList = mutableListOf(
            EmptyModel(""),
            EmptyModel(""),
            EmptyModel(""),
            EmptyModel(""),
            EmptyModel(""),
            EmptyModel(""),
            EmptyModel("")
        )
        return emptyModelList
    }

    fun getQueryInfo(query: Query): MutableLiveData<MutableList<SearchItem>> {
        //saving the user queries to the db so we can access them for an enhanced User Experience
        if (query.queryName != "null") {
            addQueryToDb(query)
        }

        //Building the url by replacing spacings with "+" so I can call the API for results
        if (query.queryName.contains(" ")) {
            query.queryName = query.queryName.replace("\\s".toRegex(), "+")
            url =
                "https://api.themoviedb.org/3/search/multi?api_key=$apiKey&query=${query.queryName}"
            Log.d("URL", url)
        } else {
            url =
                "https://api.themoviedb.org/3/search/multi?api_key=$apiKey&query=${query.queryName}"
            Log.d("URL", url)
        }

        request = Request.Builder().url(url).build()
        client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                //
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                Log.d("RESPONSE", body.toString())
                val searchItem = gson.fromJson(body, SearchItemsFeed::class.java)
                searchItems = ArrayList(searchItem.results)
                Log.d("TV", searchItems.toString())
                recentSearches.postValue(searchItems)
            }
        })
        return recentSearches
    }

    fun addQueryToDb(query: Query) {
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("userSearchQueries")
            .child(query.queryName).setValue(query)
    }

    fun getRecentUserQueries(): MutableLiveData<MutableList<Query>> {
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("userSearchQueries")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        for (i in p0.children) {
                            val query = i.getValue(Query::class.java)
                            Log.d("q", query.toString())
                            userQueries.add(query!!)
                            Log.d("movieList", userQueries.toString())
                        }
                    }
                    queries.postValue(userQueries)
                }
            })
        return queries
    }
}