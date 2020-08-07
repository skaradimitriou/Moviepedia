package com.stathis.moviepedia

import android.os.Bundle
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_movie_info_screen.*

class MovieInfoScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_info_screen)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        val rating = 8

        val ratingBar:RatingBar = findViewById(R.id.ratingBar)
        //applying rating to the ratingBar
        ratingBar.rating = rating.toFloat()/2

        likeBtn.setOnClickListener {
            addtoFavorites()
        }

        shareBtn.setOnClickListener {
            share()
        }
    }

    private fun addtoFavorites(){
        val toast = Toast.makeText(applicationContext, "FAVORITES", Toast.LENGTH_LONG)
        toast.show()
    }

    private fun share(){
        val toast = Toast.makeText(applicationContext, "SHARE", Toast.LENGTH_LONG)
        toast.show()
    }

}
