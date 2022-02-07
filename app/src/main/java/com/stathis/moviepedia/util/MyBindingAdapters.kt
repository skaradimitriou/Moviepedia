package com.stathis.moviepedia.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.stathis.moviepedia.R
import kotlinx.android.synthetic.main.upcoming_movies_item_row.view.*
import kotlin.math.roundToInt

class MyBindingAdapters {

    companion object {
        @BindingAdapter("setMovieRating")
        @JvmStatic
        fun ProgressBar.setMovieRating(rating : Double){
            this.progress = (rating * 10).roundToInt()
        }

        @BindingAdapter("setMovieStars")
        @JvmStatic
        fun RatingBar.setMovieStars(rating : Double){
            this.rating = rating.toFloat() / 2
        }

        @BindingAdapter("image","placeholder")
        @JvmStatic
        fun setImage(image: ImageView, url: String?, placeHolder: Drawable) {
            when(url.isNullOrEmpty()){
                true -> image.setImageDrawable(placeHolder)
                else -> {
                    Glide.with(image.context).load("https://image.tmdb.org/t/p/w500$url").centerCrop()
                        .placeholder(R.drawable.moviepedia_logo)
                        .into(image)
                }
            }
        }
    }
}