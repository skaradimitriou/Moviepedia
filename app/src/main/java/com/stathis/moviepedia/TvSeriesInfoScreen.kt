package com.stathis.moviepedia

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.popular_item_row.*

class TvSeriesInfoScreen : AppCompatActivity() {

    private lateinit var tvSeriesPhoto:String
    private lateinit var tvSeriesTitle:String
    private lateinit var tvSeriesRating:String
    private lateinit var tvSeriesReleaseDate:String
    private lateinit var tvSeriesDescription:String
    private lateinit var likeBtn:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tv_series_info_screen)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        likeBtn = findViewById(R.id.likeBtn)

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
            //
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

}
