package com.stathis.moviepedia.ui.dashboard.fragments.profile

import android.util.Log
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.stathis.moviepedia.listeners.FavoritesCallback
import com.stathis.moviepedia.listeners.GenericCallback
import com.stathis.moviepedia.models.movies.Movies
import com.stathis.moviepedia.models.series.TvSeries
import com.stathis.moviepedia.ui.dashboard.fragments.profile.adapter.FavoritesAdapter

class ProfileViewModel : ViewModel(), GenericCallback {

    private val dbRef by lazy { FirebaseDatabase.getInstance().reference }
    private val user by lazy { FirebaseAuth.getInstance().currentUser }

    val movies = MutableLiveData<List<Movies?>>()
    val tvSeries = MutableLiveData<List<TvSeries?>>()
    val username = MutableLiveData<String>()
    val userImage = MutableLiveData<String>()

    val adapter = FavoritesAdapter(this)
    private lateinit var callback: FavoritesCallback

    fun getUserInfo() {
        getUserData()
        getFavMovies()
    }

    fun getFavMovies() {
        dbRef.child("users")
            .child(user?.uid.toString())
            .child("favoriteMovieList")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(p0: DataSnapshot) {
                    when (p0.exists()) {
                        true -> {
                            val data = p0.children.map { it.getValue(Movies::class.java) }
                            data.let { movies.postValue(it) }
                        }
                        false -> movies.postValue(emptyList())
                    }
                }
            })
    }

    fun getFavTvSeries() {
        dbRef.child("users")
            .child(user?.uid.toString())
            .child("favoriteTvSeries")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(p0: DataSnapshot) {
                    when (p0.exists()) {
                        true -> {
                            val data = p0.children.map { it.getValue(TvSeries::class.java) }
                            data.let { tvSeries.postValue(it) }
                        }
                        false -> tvSeries.postValue(emptyList())
                    }
                }
            })
    }

    fun observe(owner: LifecycleOwner, callback: FavoritesCallback) {
        this.callback = callback

        movies.observe(owner, Observer {
            Log.d("TAG", "Fav Movies => $it")
            it?.let { adapter.submitList(it) }
        })

        tvSeries.observe(owner, Observer {
            Log.d("TAG", "Fav TvSeries => $it")
            it?.let { adapter.submitList(it) }
        })
    }

    fun release(owner: LifecycleOwner) {
        movies.removeObservers(owner)
        tvSeries.removeObservers(owner)
    }

    override fun onItemTap(view: View) = when (view.tag) {
        is Movies -> callback.onMovieTap(view.tag as Movies)
        is TvSeries -> callback.onTvSeriesTap(view.tag as TvSeries)
        else -> {
            Log.d("","")
            Unit
        }
    }

    private fun getUserData(){
        username.value = "s.karadimitriou"
        userImage.value = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTeV0v1QaL4bE4lv1A6X_7YfDw1Pz76ww-0Tj_msOTQa70VfNvybKqXER_SoPj4iS25NzQ&usqp=CAU"
    }

//
//    fun getUserPhoto() {
//        dbRef.child("users")
//            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
//            .child("userPhoto")
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(p0: DataSnapshot) {
//                    if (p0.exists()) {
//                        val imgUrl = p0.value.toString()
//                        Log.d("URL", imgUrl)
//                        imageDownloadLink.postValue(imgUrl)
//                    } else {
//                        imageDownloadLink.postValue(null)
//                    }
//                }
//
//                override fun onCancelled(p0: DatabaseError) {
//                    //
//                }
//
//            })
//    }
//
//    fun retrieveUsername() {
//        dbRef.child("users")
//            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
//            .child("username")
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onCancelled(p0: DatabaseError) {
//                    Log.d("p0", "p0")
//                }
//
//                override fun onDataChange(p0: DataSnapshot) {
//                    if (p0.exists()) {
//                        Log.d("P0", "")
//                        val name = p0.value
//                        Log.d("NAME", name.toString())
//                        username.postValue(name.toString())
//                    }
//                }
//            })
//    }
}