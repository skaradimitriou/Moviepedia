package com.stathis.moviepedia.genresInfoScreen


import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.databinding.ActivityGenresInfoScreenBinding
import com.stathis.moviepedia.models.*
import com.stathis.moviepedia.movieInfoScreen.MovieInfoScreen
import com.stathis.moviepedia.recyclerviews.ItemClickListener
import com.stathis.moviepedia.recyclerviews.MoviesAdapter
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class GenresInfoScreen : AppCompatActivity(), ItemClickListener {


    private var genreId: Int = 0
    private lateinit var genreName: String
    private lateinit var binding: ActivityGenresInfoScreenBinding
    private var genresInfoScreenViewModel: GenresInfoScreenViewModel = GenresInfoScreenViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenresInfoScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        genreId = intent.getIntExtra("GENRE_ID", genreId)
        genreName = intent.getStringExtra("GENRE_NAME")

        genresInfoScreenViewModel.getResultsForThisGenre(genreId).observe(this, Observer<MutableList<Movies>>{ movies ->
            Log.d("RESULT",movies.toString())
            binding.genresGridRecView.adapter =
                        MoviesAdapter(movies as ArrayList<Movies>, this@GenresInfoScreen)
        })

        //api call
        genresInfoScreenViewModel.getResultsForThisGenre(genreId)
        setHeader(genreName)
    }

    private fun setHeader(genreName:String){
        when (genreName) {
            "Action" -> binding.genresHeaderTxt.setBackgroundColor(Color.parseColor("#4f5fef"))
            "Adventure" -> binding.genresHeaderTxt.setBackgroundColor(Color.parseColor("#23B993"))
            "Animation" -> binding.genresHeaderTxt.setBackgroundColor(Color.parseColor("#ff0045"))
            "Comedy" -> binding.genresHeaderTxt.setBackgroundColor(Color.parseColor("#f86611"))
            "Crime" -> binding.genresHeaderTxt.setBackgroundColor(Color.parseColor("#EC5657"))
            "Documentary" -> binding.genresHeaderTxt.setBackgroundColor(Color.parseColor("#2D2C4E"))
            "Drama" -> binding.genresHeaderTxt.setBackgroundColor(Color.parseColor("#000000"))
            "Family" -> binding.genresHeaderTxt.setBackgroundColor(Color.parseColor("#4f5fef"))
            "Fantasy" -> binding.genresHeaderTxt.setBackgroundColor(Color.parseColor("#23B993"))
            "History" -> binding.genresHeaderTxt.setBackgroundColor(Color.parseColor("#ff0045"))
            "Horror" -> binding.genresHeaderTxt.setBackgroundColor(Color.parseColor("#f86611"))
            "Music" -> binding.genresHeaderTxt.setBackgroundColor(Color.parseColor("#EC5657"))
            "Mystery" -> binding.genresHeaderTxt.setBackgroundColor(Color.parseColor("#2D2C4E"))
            "Romance" -> binding.genresHeaderTxt.setBackgroundColor(Color.parseColor("#000000"))
            "Science Fiction" -> binding.genresHeaderTxt.setBackgroundColor(Color.parseColor("#4f5fef"))
            "TV Movie" -> binding.genresHeaderTxt.setBackgroundColor(Color.parseColor("#23B993"))
            "Thriller" -> binding.genresHeaderTxt.setBackgroundColor(Color.parseColor("#ff0045"))
            "War" -> binding.genresHeaderTxt.setBackgroundColor(Color.parseColor("#f86611"))
            "Western" -> binding.genresHeaderTxt.setBackgroundColor(Color.parseColor("#EC5657"))
        }
        binding.genresHeaderTxt.text = "$genreName Movies"
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
