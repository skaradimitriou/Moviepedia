package com.stathis.moviepedia.ui.movieInfoScreen

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.stathis.moviepedia.R
import com.stathis.moviepedia.databinding.ActivityMovieInfoScreenBinding
import com.stathis.moviepedia.models.FavoriteMovies
import com.stathis.moviepedia.models.Reviews
import com.stathis.moviepedia.models.cast.Cast
import com.stathis.moviepedia.adapters.CastAdapter
import com.stathis.moviepedia.adapters.ReviewsAdapter

class MovieInfoScreen : AppCompatActivity() {

    private var movieId: Int = 0
    private lateinit var moviePhoto: String
    private lateinit var movieTitle: String
    private lateinit var movieRating: String
    private lateinit var movieReleaseDate: String
    private lateinit var movieDescription: String
    private lateinit var databaseReference: DatabaseReference
    private lateinit var binding: ActivityMovieInfoScreenBinding
    private var viewModel = MovieInfoScreenViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieInfoScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        getIntentInfo()
        getFavoritesFromDb()

        viewModel.getMovieCastInfo(movieId)
        viewModel.getMovieReviews(movieId)

        binding.castRecView.adapter = viewModel.adapter
        binding.reviewsRecView.adapter = viewModel.reviewsAdapter

        viewModel.observeData(this)

        binding.likeBtn.setOnClickListener {
            val whiteFavBtnColor: Drawable.ConstantState? =
                resources.getDrawable(R.drawable.ic_favorite_icon, this.theme).constantState
            val likeBtnColor = binding.likeBtn.drawable.constantState

            if (likeBtnColor!!.equals(whiteFavBtnColor)) {
                addToFavorites()
            } else {
                removeFromFavorites()
            }
        }

        binding.shareBtn.setOnClickListener {
            share()
        }
    }

    private fun getIntentInfo() {
        movieId = intent.getIntExtra("MOVIE_ID", movieId)
        moviePhoto = intent.getStringExtra("MOVIE_PHOTO")

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500$moviePhoto")
            .placeholder(R.drawable.default_img)
            .into(binding.imgView)

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500$moviePhoto")
            .placeholder(R.drawable.default_img)
            .into(binding.movieImg)

        binding.mainTxt.text = intent.getStringExtra("MOVIE_NAME") ?: ""

        var rating = intent.getStringExtra("RATING").toDouble()
        binding.ratingBar.rating = rating.toFloat() / 2

        binding.description.text = intent.getStringExtra("DESCRIPTION") ?: ""
        binding.releaseDate.text = intent.getStringExtra("RELEASE_DATE") ?: ""
    }

    private fun getFavoritesFromDb() {
        //adds a new favorite to the favorite movie list
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("favoriteMovieList")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        for (i in p0.children) {
                            val fav = i.getValue(FavoriteMovies::class.java)
                            if (fav?.title == movieTitle) {
                                binding.likeBtn.setImageResource(R.drawable.ic_favorite_white)
                            }
                            Log.d("i", i.toString())
                        }
                    }
                }
            })
    }

    private fun removeFromFavorites() {
        //adds a new favorite to the favorite movie list
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("favoriteMovieList")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        for (i in p0.children) {
                            val favorite = i.getValue(FavoriteMovies::class.java)
                            if (favorite?.title == movieTitle) {
                                i.ref.removeValue()
                                binding.likeBtn.setImageResource(R.drawable.ic_favorite_icon)
                            }
                        }
                    }
                }
            })
    }

    private fun addToFavorites() {
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("favoriteMovieList")
            .child(movieTitle).setValue(
                FavoriteMovies(
                    movieId,
                    moviePhoto,
                    movieTitle,
                    movieRating.toDouble(),
                    movieDescription,
                    movieReleaseDate
                )
            )
        binding.likeBtn.setImageResource(R.drawable.ic_favorite_white)
    }

    private fun share() {
        startActivity(Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, movieTitle)
        })
    }
}