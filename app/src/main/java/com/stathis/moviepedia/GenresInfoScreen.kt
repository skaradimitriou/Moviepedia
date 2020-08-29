package com.stathis.moviepedia


import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
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
    private lateinit var header:TextView
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

        header = findViewById(R.id.genresHeaderTxt)

        //api call
        getResultsForThisGenre()

        moviesGridRecView = findViewById(R.id.genresGridRecView)
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
                    val genre = MovieGenres(genreId,genreName)
                    //setting background color according to the Genre ID
                    setGenreHeader(genre)
                    header.text = "$genreName Movies"
                    moviesGridRecView.adapter = MoviesAdapter(movieList as ArrayList<Movies>,this@GenresInfoScreen)
                }
            }
        })
    }

    private fun setGenreHeader(genres: MovieGenres){
        when(genres.name){
            "Action" -> header.setBackgroundColor(Color.parseColor("#4f5fef"))
            "Adventure" -> header.setBackgroundColor(Color.parseColor("#23B993"))
            "Animation" -> header.setBackgroundColor(Color.parseColor("#ff0045"))
            "Comedy" -> header.setBackgroundColor(Color.parseColor("#f86611"))
            "Crime" -> header.setBackgroundColor(Color.parseColor("#EC5657"))
            "Documentary" -> header.setBackgroundColor(Color.parseColor("#2D2C4E"))
            "Drama" -> header.setBackgroundColor(Color.parseColor("#000000"))
            "Family" -> header.setBackgroundColor(Color.parseColor("#4f5fef"))
            "Fantasy" -> header.setBackgroundColor(Color.parseColor("#23B993"))
            "History" -> header.setBackgroundColor(Color.parseColor("#ff0045"))
            "Horror" -> header.setBackgroundColor(Color.parseColor("#f86611"))
            "Music" -> header.setBackgroundColor(Color.parseColor("#EC5657"))
            "Mystery" -> header.setBackgroundColor(Color.parseColor("#2D2C4E"))
            "Romance" -> header.setBackgroundColor(Color.parseColor("#000000"))
            "Science Fiction" -> header.setBackgroundColor(Color.parseColor("#4f5fef"))
            "TV Movie" -> header.setBackgroundColor(Color.parseColor("#23B993"))
            "Thriller" -> header.setBackgroundColor(Color.parseColor("#ff0045"))
            "War" -> header.setBackgroundColor(Color.parseColor("#f86611"))
            "Western" -> header.setBackgroundColor(Color.parseColor("#EC5657"))
        }
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
