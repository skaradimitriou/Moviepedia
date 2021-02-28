package com.stathis.moviepedia.ui.genresInfoScreen


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.stathis.moviepedia.databinding.ActivityGenresInfoScreenBinding
import com.stathis.moviepedia.ui.movieInfoScreen.MovieInfoScreen

class GenresInfoScreen : AppCompatActivity() {

    private var genreId: Int = 0
    private lateinit var genreName: String
    private lateinit var binding: ActivityGenresInfoScreenBinding
    private lateinit var viewModel: GenresInfoScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenresInfoScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        viewModel = ViewModelProvider.AndroidViewModelFactory(application)
            .create(GenresInfoScreenViewModel::class.java)

        genreId = intent.getIntExtra("GENRE_ID", genreId)
        genreName = intent.getStringExtra("GENRE_NAME")

        viewModel.getResultsForThisGenre(genreId)

        setHeader(genreName)
        binding.genresGridRecView.adapter = viewModel.adapter

        viewModel.observeData(this)

        viewModel.movie.observe(this, Observer { movie ->
            startActivity(Intent(this, MovieInfoScreen::class.java).also{
                if (movie.name.isNullOrBlank()) {
                    it.putExtra("MOVIE_NAME", movie.title)
                } else {
                    it.putExtra("MOVIE_NAME", movie.name)
                }
                it.putExtra("MOVIE_ID", movie.id)
                it.putExtra("MOVIE_PHOTO", movie.backdrop_path)
                it.putExtra("MOVIE_PHOTO", movie.poster_path)
                it.putExtra("RELEASE_DATE", movie.release_date)
                it.putExtra("DESCRIPTION", movie.overview)
                it.putExtra("RATING", movie.vote_average.toString())
            })
        })
    }

    private fun setHeader(genreName: String) {
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
}
