package com.stathis.moviepedia

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.models.FavoriteMovies
import com.stathis.moviepedia.models.MovieFeed
import com.stathis.moviepedia.models.Movies
import com.stathis.moviepedia.models.cast.Cast
import com.stathis.moviepedia.models.cast.Crew
import com.stathis.moviepedia.models.cast.MovieCastFeed
import com.stathis.moviepedia.recyclerviews.CastAdapter
import com.stathis.moviepedia.recyclerviews.PopularMoviesAdapter
import kotlinx.android.synthetic.main.activity_movie_info_screen.*
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class MovieInfoScreen : AppCompatActivity() {

    private var movieId:Int = 0
    private lateinit var moviePhoto:String
    private lateinit var movieTitle:String
    private lateinit var movieRating:String
    private lateinit var movieReleaseDate:String
    private lateinit var movieDescription:String
    private lateinit var url: String
    private lateinit var request: Request
    private lateinit var client: OkHttpClient
    private var movieCastInfo: MutableList<Cast> = mutableListOf()
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_info_screen)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        var likeBtn:ImageView = findViewById(R.id.likeBtn)

        //getting info about the movie from intent
        getIntentInfo()
        getFavoritesFromDb()

        getMovieCastInfo()

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

    private fun getIntentInfo(){
        movieId = intent.getIntExtra("MOVIE_ID",movieId)
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

    private fun getMovieCastInfo() {
        url =
            "https://api.themoviedb.org/3/movie/$movieId/credits?api_key=b36812048cc4b54d559f16a2ff196bc5"
        request = Request.Builder().url(url).build()

        client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                val cast = gson.fromJson(body, MovieCastFeed::class.java)
                Log.d("Response", cast.toString())
                movieCastInfo = ArrayList(cast.cast)
                Log.d("this is the list",movieCastInfo.toString())

                runOnUiThread{
                    val castRecView:RecyclerView = findViewById(R.id.castRecView)
                    castRecView.adapter = CastAdapter(movieCastInfo)
                }
            }
        })
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
                            val fav = i.getValue(FavoriteMovies::class.java)
                            if(fav?.title == movieTitle){
                                likeBtn.setImageResource(R.drawable.ic_favorite_white)
                            }
                            Log.d("i",i.toString())
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
            databaseReference = FirebaseDatabase.getInstance().reference
            databaseReference.child("users")
                .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                .child("favoriteMovieList")
                .child(movieTitle).setValue(FavoriteMovies(moviePhoto,movieTitle,movieRating.toDouble(),movieDescription,movieReleaseDate))
            val likeBtn:ImageView  = findViewById(R.id.likeBtn)
            likeBtn.setImageResource(R.drawable.ic_favorite_white)
    }

    private fun share(){
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type="text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        startActivity(shareIntent)
    }

}