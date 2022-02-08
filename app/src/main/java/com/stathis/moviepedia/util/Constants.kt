package com.stathis.moviepedia

import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import kotlin.math.roundToInt

const val BASE_URL = "https://api.themoviedb.org/3/"
const val API_KEY = "api_key=b36812048cc4b54d559f16a2ff196bc5"
const val ORIGINAL_PHOTO_URL = "https://image.tmdb.org/t/p/original"

@BindingAdapter("setMovieRating")
fun ProgressBar.setMovieRating(rating : Double){
    this.progress = (rating * 10).roundToInt()
}