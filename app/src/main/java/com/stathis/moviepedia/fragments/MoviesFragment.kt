package com.stathis.moviepedia.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.MovieFeed
import com.stathis.moviepedia.models.Movies
import com.stathis.moviepedia.recyclerviews.MoviesAdapter
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException


class MoviesFragment : Fragment() {

    private lateinit var url: String
    private lateinit var request: Request
    private lateinit var client: OkHttpClient
    private lateinit var databaseReference: DatabaseReference
    private var movieList: MutableList<Movies> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAllMovies()
        getMoviesFromDb()

    }

    private fun getMoviesFromDb(){
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("moviesList")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        for (i in p0.children) {
                            val movie = i.getValue(Movies::class.java)
                            movieList.add(movie!!)
                            Log.d("movieList", movieList.toString())

                            val moviesGridRecView: RecyclerView = view!!.findViewById(R.id.moviesGridRecView)
                            moviesGridRecView.adapter = MoviesAdapter(movieList as ArrayList<Movies>)
                        }
                    }
                }
            })
    }

    fun getAllMovies() {

        url = "https://api.themoviedb.org/3/trending/all/day?api_key=b36812048cc4b54d559f16a2ff196bc5"
        request = Request.Builder().url(url).build()

        client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                val popularMovies = gson.fromJson(body, MovieFeed::class.java)
                Log.d("Response", popularMovies.toString())
                val moviesList: ArrayList<Movies> = ArrayList(popularMovies.results)

                databaseReference = FirebaseDatabase.getInstance().reference
                databaseReference.child("users")
                    .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                    .child("moviesList").setValue(moviesList)
            }
        })
    }

}
