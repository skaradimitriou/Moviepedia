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

    private lateinit var moviePhoto: String
    private lateinit var movieTitle: String
    private lateinit var movieRating: String
    private lateinit var movieReleaseDate: String
    private lateinit var movieDescription: String
    private lateinit var databaseReference: DatabaseReference
    private lateinit var binding: ActivityMovieInfoScreenBinding
    private var movieInfoScreenViewModel = MovieInfoScreenViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieInfoScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        //getting info about the movie from intent
        getIntentInfo()
        getFavoritesFromDb()

        var movieId: Int = 1
        movieId = intent.getIntExtra("MOVIE_ID", movieId)

        movieInfoScreenViewModel.getMovieCastInfo(movieId)
            .observe(this, androidx.lifecycle.Observer<MutableList<Cast>> { cast ->
                Log.d("CAST", cast.toString())
                binding.castRecView.adapter = CastAdapter(cast)
            })

        movieInfoScreenViewModel.getMovieReviews(movieId)
            .observe(this, androidx.lifecycle.Observer<MutableList<Reviews>> {
                val reviewsAdapter = ReviewsAdapter()
                reviewsAdapter.submitList(it as List<Any>?)
                binding.reviewsRecView.adapter = reviewsAdapter
            })

        movieInfoScreenViewModel.getMovieCastInfo(movieId)
        movieInfoScreenViewModel.getMovieReviews(movieId)

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
        moviePhoto = intent.getStringExtra("MOVIE_PHOTO")
        movieTitle = intent.getStringExtra("MOVIE_NAME")
        movieRating = intent.getStringExtra("RATING")
        movieDescription = intent.getStringExtra("DESCRIPTION")
        movieReleaseDate = intent.getStringExtra("RELEASE_DATE")

        setMoviePhoto()
        setMovieTitle()
        setMovieRating()
        setMovieDescription()
        setMovieReleaseDate()
    }

    private fun setMoviePhoto() {
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500$moviePhoto")
            .into(binding.imgView)
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500$moviePhoto")
            .into(binding.movieImg)
    }

    private fun setMovieTitle() {
        binding.mainTxt.text = movieTitle
    }

    private fun setMovieRating() {
        //converting rating toDouble()
        var rating = intent.getStringExtra("RATING").toDouble()
        //applying rating to the ratingBar
        binding.ratingBar.rating = rating.toFloat() / 2
    }

    private fun setMovieDescription() {
        binding.description.text = movieDescription
    }

    private fun setMovieReleaseDate() {
        binding.releaseDate.text = movieReleaseDate
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
        startActivity(Intent().apply{
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
        })
    }
}