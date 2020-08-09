package com.stathis.moviepedia

import android.media.Image
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.stathis.moviepedia.models.FavoriteMovies
import com.stathis.moviepedia.models.MovieGenres
import com.stathis.moviepedia.models.Movies
import kotlinx.android.synthetic.main.activity_movie_info_screen.*
import org.w3c.dom.Text

class MovieInfoScreen : AppCompatActivity() {

    private lateinit var moviePhoto:String
    private lateinit var movieTitle:String
    private lateinit var movieRating:String
    private lateinit var movieReleaseDate:String
    private lateinit var movieDescription:String
    private lateinit var databaseReference: DatabaseReference
    private var favoriteMoviesList: MutableList<FavoriteMovies> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_info_screen)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

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

        likeBtn.setOnClickListener {
            addtoFavorites()
        }

        shareBtn.setOnClickListener {
            share()
        }
    }

    private fun setMoviePhoto(){
        var movieImg:ImageView = findViewById(R.id.imgView)

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500$moviePhoto")
            .into(movieImg)
    }

    private fun setMovieTitle(){
        var title:TextView = findViewById(R.id.mainTxt)
        title.text = movieTitle
    }

    private fun setMovieRating(){
        //converting rating toDouble()
        var rating = intent.getStringExtra("RATING").toDouble()
        var ratingTxt:TextView = findViewById(R.id.rating)
        ratingTxt.text = "$rating/10"
        val ratingBar:RatingBar = findViewById(R.id.ratingBar)
        //applying rating to the ratingBar
        ratingBar.rating = rating.toFloat()/2
    }

    private fun setMovieDescription(){
        var description: TextView = findViewById(R.id.description)
        description.text = movieDescription
    }

    private fun setMovieReleaseDate(){
        var releaseDate:TextView = findViewById(R.id.releaseDate)
        releaseDate.text = movieReleaseDate
    }

    private fun addtoFavorites(){
        //adds a new favorite to the favorite movie list
        TODO("MAKE IT POSSIBLE TO ADD MULTIPLE FAVORITE MOVIES")
        favoriteMoviesList.add(FavoriteMovies(moviePhoto,movieTitle,movieRating.toDouble(),movieDescription,movieReleaseDate))
        Log.d("FAV_MOVIE",favoriteMoviesList.toString())

        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("favoriteMovieList").setValue(favoriteMoviesList)
    }

    private fun share(){
        val toast = Toast.makeText(applicationContext, "SHARE", Toast.LENGTH_LONG)
        toast.show()
    }

}
