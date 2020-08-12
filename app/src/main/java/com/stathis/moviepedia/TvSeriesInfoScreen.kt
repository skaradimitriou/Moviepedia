package com.stathis.moviepedia

import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.stathis.moviepedia.models.FavoriteMovies
import com.stathis.moviepedia.models.FavoriteTvSeries
import kotlinx.android.synthetic.main.activity_movie_info_screen.*
import kotlinx.android.synthetic.main.activity_movie_info_screen.shareBtn
import kotlinx.android.synthetic.main.activity_tv_series_info_screen.*
import kotlinx.android.synthetic.main.popular_item_row.*

class TvSeriesInfoScreen : AppCompatActivity() {

    private var tvSeriesId:Int = 0
    private lateinit var tvSeriesPhoto:String
    private lateinit var tvSeriesTitle:String
    private lateinit var tvSeriesRating:String
    private lateinit var tvSeriesReleaseDate:String
    private lateinit var tvSeriesDescription:String
    private lateinit var databaseReference:DatabaseReference
    private lateinit var likeBtn:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tv_series_info_screen)
        getUserFavorites()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        likeBtn = findViewById(R.id.likeBtn)

        tvSeriesId = intent.getIntExtra("TV_SERIES_ID",tvSeriesId)
        tvSeriesPhoto = intent.getStringExtra("TV_SERIES_PHOTO")
        tvSeriesTitle = intent.getStringExtra("TV_SERIES_NAME")
        tvSeriesRating = intent.getStringExtra("TV_SERIES_RATING")
        tvSeriesReleaseDate = intent.getStringExtra("TV_SERIES_RELEASE_DATE")
        tvSeriesDescription = intent.getStringExtra("TV_SERIES_DESCRIPTION")

        setTvSeriesPhoto()
        setTvSeriesTitle()
        setTvSeriesRating()
        setTvSeriesDescription()
        setTvSeriesReleaseDate()

        likeBtn.setOnClickListener{
            val whiteFavBtnColor: Drawable.ConstantState? = resources.getDrawable(R.drawable.ic_favorite_icon,this.theme).constantState
            val likeBtnColor = likeBtn.drawable.constantState

            if(likeBtnColor!!.equals(whiteFavBtnColor)){
                addToFavorites()
            }
            else {
                removeFromFavorites()
            }
        }

        shareBtn.setOnClickListener{
            share()
        }

    }

    private fun setTvSeriesPhoto(){
        var backgroundImg:ImageView = findViewById(R.id.imgView)

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500$tvSeriesPhoto")
            .into(backgroundImg)
    }

    private fun setTvSeriesTitle(){
        var title: TextView = findViewById(R.id.tvSeriesTxt)
        title.text = tvSeriesTitle
    }

    private fun setTvSeriesRating(){
        //converting rating toDouble()
        var rating = tvSeriesRating.toDouble()
        var ratingTxt:TextView = findViewById(R.id.rating)
        ratingTxt.text = "$rating/10"
        val ratingBar: RatingBar = findViewById(R.id.ratingBar)
        //applying rating to the ratingBar
        ratingBar.rating = rating.toFloat()/2
    }

    private fun setTvSeriesDescription(){
        var description: TextView = findViewById(R.id.description)
        description.text = tvSeriesDescription
    }

    private fun setTvSeriesReleaseDate(){
        var releaseDate:TextView = findViewById(R.id.releaseDate)
        releaseDate.text = tvSeriesReleaseDate
    }

    private fun addToFavorites(){
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("favoriteTvSeries")
            .child(tvSeriesId.toString()).setValue(FavoriteTvSeries(tvSeriesId,tvSeriesPhoto,tvSeriesTitle,tvSeriesRating.toDouble(),tvSeriesReleaseDate,tvSeriesDescription))
        val likeBtn:ImageView  = findViewById(R.id.likeBtn)
        likeBtn.setImageResource(R.drawable.ic_favorite_white)
    }

    private fun getUserFavorites(){
        //adds a new favorite to the favorite movie list
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("favoriteTvSeries")
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        for (i in p0.children) {
                            val fav = i.getValue(FavoriteTvSeries::class.java)
                            if(fav?.id == tvSeriesId){
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
            .child("favoriteTvSeries")
            .addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    //
                }
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        for (i in p0.children) {
                            val favorite = i.getValue(FavoriteTvSeries::class.java)
                            if(favorite?.id == tvSeriesId){
                                i.ref.removeValue()
                                likeBtn.setImageResource(R.drawable.ic_favorite_icon)
                            }
                        }
                    }
                }
            })
    }

    private fun share(){
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type="text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        startActivity(shareIntent)
    }

}
