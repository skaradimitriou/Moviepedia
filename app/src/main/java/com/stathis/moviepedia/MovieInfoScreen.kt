package com.stathis.moviepedia

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.stathis.moviepedia.R.drawable.ic_favorite_icon
import com.stathis.moviepedia.models.FavoriteMovies
import com.stathis.moviepedia.models.MovieGenres
import com.stathis.moviepedia.models.Movies
import com.stathis.moviepedia.recyclerviews.MoviesAdapter
import kotlinx.android.synthetic.main.activity_movie_info_screen.*
import org.w3c.dom.Comment
import org.w3c.dom.Text

class MovieInfoScreen : AppCompatActivity() {

    private lateinit var moviePhoto:String
    private lateinit var movieTitle:String
    private lateinit var movieRating:String
    private lateinit var movieReleaseDate:String
    private lateinit var movieDescription:String
    private lateinit var databaseReference: DatabaseReference
    private var favoriteMoviesList: MutableList<FavoriteMovies> = mutableListOf()
    private var test: MutableList<FavoriteMovies> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_info_screen)
        getFavoritesFromDb()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        var likeBtn:ImageView = findViewById(R.id.likeBtn)

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
            val whiteFavBtnColor: Drawable.ConstantState? = resources.getDrawable(R.drawable.ic_favorite_icon,this.theme).constantState
            val likeBtnColor = likeBtn.drawable.constantState

            if(likeBtnColor!!.equals(whiteFavBtnColor)){
               addToFavorites()
            }
            else {
                removeFromFavorites()
            }
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

    private fun getFavoritesFromDb() {
        //adds a new favorite to the favorite movie list
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("favoriteMovieList")
            .addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    //
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        for (i in p0.children) {
                            val favorite = i.getValue(FavoriteMovies::class.java)
                            if(favorite?.title == movieTitle){
                                likeBtn.setImageResource(R.drawable.ic_favorite_black_24dp)
                                favoriteMoviesList.add(favorite)
                                Log.d("IS FAV", favoriteMoviesList.toString())
                            }
                            favoriteMoviesList.add(favorite!!)
                            Log.d("favoriteList", favoriteMoviesList.toString())
                        }
                    }
                }
            })
    }

    private fun removeFromFavorites(){
        //adds a new favorite to the favorite movie list
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("favoriteMovieList")
            .addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    //
                }
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        for (i in p0.children) {
                            val favorite = i.getValue(FavoriteMovies::class.java)
                            val fav = FavoriteMovies(moviePhoto,movieTitle,movieRating.toDouble(),movieDescription,movieReleaseDate)
                            if(favorite?.title == movieTitle){
                                i.ref.removeValue()
                                likeBtn.setImageResource(R.drawable.ic_favorite_icon)
                            }
                        }
                    }
                }
            })
    }

    private fun addToFavorites(){

        favoriteMoviesList.add(FavoriteMovies(moviePhoto,movieTitle,movieRating.toDouble(),movieDescription,movieReleaseDate))
        Log.d("FAV_MOVIE",favoriteMoviesList.toString())

        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("favoriteMovieList").setValue(favoriteMoviesList)
        val likeBtn:ImageView  = findViewById(R.id.likeBtn)
        likeBtn.setImageResource(R.drawable.ic_favorite_black_24dp)
    }

    private fun share(){
        val toast = Toast.makeText(applicationContext, "SHARE", Toast.LENGTH_LONG)
        toast.show()
    }

}
