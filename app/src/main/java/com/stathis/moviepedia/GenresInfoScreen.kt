package com.stathis.moviepedia


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.models.*
import com.stathis.moviepedia.recyclerviews.ItemClickListener
import com.stathis.moviepedia.recyclerviews.MoviesAdapter
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class GenresInfoScreen : AppCompatActivity(), ItemClickListener {

    private lateinit var url: String
    private lateinit var request: Request
    private lateinit var client: OkHttpClient
    private var genreId: Int = 0
    private lateinit var genreName: String
    private val apiKey = "?api_key=b36812048cc4b54d559f16a2ff196bc5"
    private var movieList: MutableList<Movies> = mutableListOf()
    private lateinit var moviesGridRecView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genres_info_screen)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        genreId = intent.getIntExtra("GENRE_ID",genreId)
        genreName = intent.getStringExtra("GENRE_NAME")

        moviesGridRecView = findViewById(R.id.genresGridRecView)

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
                    moviesGridRecView.adapter = MoviesAdapter(movieList as ArrayList<Movies>,this@GenresInfoScreen)
                }
            }
        })
    }

    override fun onItemClick(movies: Movies) {
        val movieIntent = Intent(this, MovieInfoScreen::class.java)
        val name = movies.name
        val title = movies.title
        //converting rating toString() so I can pass it. Double was throwing error
        val rating = movies.vote_average.toString()
        Log.d("rating", rating)
        if (name.isNullOrBlank()) {
            movieIntent.putExtra("MOVIE_NAME", title)
            Log.d("Movie Name Clicked", title)
        } else {
            movieIntent.putExtra("MOVIE_NAME", name)
            Log.d("Movie Name Clicked", name)
        }
        movieIntent.putExtra("MOVIE_ID", movies.id)
        movieIntent.putExtra("MOVIE_PHOTO", movies.backdrop_path)
        movieIntent.putExtra("MOVIE_PHOTO", movies.poster_path)
        movieIntent.putExtra("RELEASE_DATE", movies.release_date)
        movieIntent.putExtra("DESCRIPTION", movies.overview)
        movieIntent.putExtra("RATING", rating)
        startActivity(movieIntent)
    }

    override fun onTvSeriesClick(tvSeries: TvSeries) {
        //
    }

    override fun onClick(v: View?) {
        //
    }

}
