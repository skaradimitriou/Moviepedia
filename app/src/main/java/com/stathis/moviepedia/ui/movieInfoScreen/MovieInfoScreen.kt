package com.stathis.moviepedia.ui.movieInfoScreen

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.stathis.moviepedia.R
import com.stathis.moviepedia.abstraction.AbstractActivity
import com.stathis.moviepedia.databinding.ActivityMovieInfoScreenBinding
import com.stathis.moviepedia.models.FavoriteMovies

class MovieInfoScreen : AbstractActivity() {

    private var movieId: Int = 0
    private lateinit var moviePhoto: String
    private lateinit var movieTitle: String
    private lateinit var movieRating: String
    private lateinit var movieReleaseDate: String
    private lateinit var movieDescription: String

    private lateinit var binding: ActivityMovieInfoScreenBinding
    private lateinit var viewModel: MovieInfoScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieInfoScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initLayout() {
        viewModel = ViewModelProvider(this).get(MovieInfoScreenViewModel::class.java)
    }

    override fun running() {
        getIntentInfo()
        viewModel.getFavoritesFromDb(movieTitle)

        viewModel.getMovieCastInfo(movieId)
        viewModel.getMovieReviews(movieId)

        binding.castRecView.adapter = viewModel.adapter
        binding.reviewsRecView.adapter = viewModel.reviewsAdapter

        binding.likeBtn.setOnClickListener {
            val whiteFavBtnColor: Drawable.ConstantState? =
                resources.getDrawable(R.drawable.ic_favorite_icon, this.theme).constantState
            val likeBtnColor = binding.likeBtn.drawable.constantState

            if (likeBtnColor!!.equals(whiteFavBtnColor)) {
                val fav = FavoriteMovies(
                    movieId,
                    moviePhoto,
                    movieTitle,
                    movieRating.toDouble(),
                    movieDescription,
                    movieReleaseDate
                )
                viewModel.addToFavorites(fav)
            } else {
                viewModel.removeFromFavorites(movieTitle)
            }
        }

        viewModel.observeData(this)

        viewModel.isFavorite.observe(this, Observer {
            when (it) {
                true -> {
                    binding.likeBtn.setImageResource(R.drawable.ic_favorite_white)
                }

                false -> {
                    binding.likeBtn.setImageResource(R.drawable.ic_favorite_icon)
                }
            }
        })

        binding.shareBtn.setOnClickListener {
            share()
        }
    }

    override fun stopped() {
        viewModel.removeObservers(this)
        viewModel.isFavorite.removeObservers(this)
    }

    private fun getIntentInfo() {
        movieId = intent.getIntExtra("MOVIE_ID", movieId)
        moviePhoto = intent.getStringExtra("MOVIE_PHOTO")

        movieTitle = intent.getStringExtra("MOVIE_NAME") ?: ""
        binding.mainTxt.text = movieTitle

        movieDescription = intent.getStringExtra("DESCRIPTION") ?: ""
        binding.description.text = movieDescription

        movieReleaseDate = intent.getStringExtra("RELEASE_DATE") ?: ""
        binding.releaseDate.text = movieReleaseDate

        movieRating = intent.getStringExtra("RATING")
        var rating = intent.getStringExtra("RATING").toDouble()
        binding.ratingBar.rating = rating.toFloat() / 2

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500$moviePhoto")
            .placeholder(R.drawable.default_img)
            .into(binding.imgView)

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500$moviePhoto")
            .placeholder(R.drawable.default_img)
            .into(binding.movieImg)
    }

    private fun share() {
        startActivity(Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, movieTitle)
        })
    }
}