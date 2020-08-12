package com.stathis.moviepedia


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.models.GenreMoviesFeed
import com.stathis.moviepedia.models.MovieFeed
import com.stathis.moviepedia.models.MovieGenresFeed
import com.stathis.moviepedia.models.Movies
import com.stathis.moviepedia.recyclerviews.MoviesAdapter
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class GenresInfoScreen : AppCompatActivity() {

    private lateinit var url: String
    private lateinit var request: Request
    private lateinit var client: OkHttpClient
    private var genreId: Int = 0
    private lateinit var genreName: String
    private val apiKey = "?api_key=b36812048cc4b54d559f16a2ff196bc5"
    private var movieList: MutableList<Movies> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genres_info_screen)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        genreId = intent.getIntExtra("GENRE_ID",genreId)
        genreName = intent.getStringExtra("GENRE_NAME")

        getResultsForThisGenre()

    }

    private fun getResultsForThisGenre() {
        url = "https://api.themoviedb.org/3/discover/movie$apiKey&with_genres=$genreId"
        request = Request.Builder().url(url).build()

        client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                val genres = gson.fromJson(body, GenreMoviesFeed::class.java)
                Log.d("Response", genres.toString())
                movieList = ArrayList(genres.results)
                Log.d("GENRE_LIST",movieList.toString())

                runOnUiThread{
                    val header:TextView = findViewById(R.id.genresHeaderTxt)
                    header.text = "$genreName Movies"
                    val moviesGridRecView: RecyclerView = findViewById(R.id.genresGridRecView)
                    moviesGridRecView.adapter = MoviesAdapter(movieList as ArrayList<Movies>)
                }
            }
        })
    }

}
