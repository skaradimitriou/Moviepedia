package com.stathis.moviepedia.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.stathis.moviepedia.R
import kotlin.math.roundToInt

class MyBindingAdapters {

    companion object {
        @BindingAdapter("setMovieRating")
        @JvmStatic
        fun ProgressBar.setMovieRating(rating : Double){
            this.progress = (rating * 10).roundToInt()
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