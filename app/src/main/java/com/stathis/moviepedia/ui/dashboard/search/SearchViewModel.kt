package com.stathis.moviepedia.ui.dashboard.search

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.stathis.moviepedia.listeners.GenericCallback
import com.stathis.moviepedia.listeners.old.SearchItemClickListener
import com.stathis.moviepedia.models.EmptyModel
import com.stathis.moviepedia.network.ApiClient
import com.stathis.moviepedia.ui.dashboard.search.adapter.SearchAdapter
import com.stathis.moviepedia.ui.dashboard.search.models.Query
import com.stathis.moviepedia.ui.dashboard.search.models.SearchItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel(), GenericCallback {

    val adapter = SearchAdapter(this)
    val recentSearches = MutableLiveData<List<SearchItem>>()
    val queries = MutableLiveData<List<Query?>>()

    private val dbRef = FirebaseDatabase.getInstance().reference
    private val myUser = FirebaseAuth.getInstance().currentUser
    private lateinit var callback: SearchItemClickListener

    fun getQueryInfo(query: Query) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                ApiClient.getQueryInfo(query.queryName)
            }.onSuccess {
                when (it.isSuccessful) {
                    true -> recentSearches.postValue(it.body()?.results)
                    else -> {} //handle error case
                }
            }
        }


        //saving the user queries to the db so we can access them for an enhanced User Experience
        if (query.queryName != "null") {
            addQueryToDb(query)
        }

        //Building the url by replacing spacings with "+" so I can call the API for results
        if (query.queryName.contains(" ")) {
            query.queryName = query.queryName.replace("\\s".toRegex(), "+")
        }
    }

    private fun addQueryToDb(query: Query) {
        dbRef.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("userSearchQueries")
            .child(query.queryName).setValue(query)
    }

    fun getRecentUserQueries() {
        dbRef.child("users")
            .child(myUser?.uid.toString())
            .child("userSearchQueries")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        val data = p0.children.map { it.getValue(Query::class.java) }
                        queries.postValue(data)
                    }
                }
            })
    }

    fun observeData(owner: LifecycleOwner, callback : SearchItemClickListener) {
        this.callback = callback

        queries.observe(owner, Observer { queries ->
            Log.d("Queries:", queries.toString())
            adapter.submitList(queries)
        })

        recentSearches.observe(owner, Observer {
            Log.d("Search Items:", it.toString())
            adapter.submitList(it)
        })
    }

    fun removeObservers(owner: LifecycleOwner) {
        queries.removeObservers(owner)
        recentSearches.removeObservers(owner)
    }

    override fun onItemTap(view: View) = when (view.tag) {
        is SearchItem -> callback.onSearchItemClick(view.tag as SearchItem)
        is Query -> callback.onQueryClick(view.tag as Query)
        else -> Unit
    }
}