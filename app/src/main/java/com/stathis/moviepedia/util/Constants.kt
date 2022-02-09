package com.stathis.moviepedia

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import kotlin.math.roundToInt

const val BASE_URL = "https://api.themoviedb.org/3/"
const val API_KEY = "api_key=b36812048cc4b54d559f16a2ff196bc5"
const val ORIGINAL_PHOTO_URL = "https://image.tmdb.org/t/p/original"

@BindingAdapter("setMovieRating")
fun ProgressBar.setMovieRating(rating: Double) {
    this.progress = (rating * 10).roundToInt()
}

@BindingAdapter("setMovieStarRating")
fun TextView.setMovieStar(rating: Double) {
    val rating = rating.toFloat() / 2
    this.text = String.format("⭐ %.1f", rating)
}

@BindingAdapter("setMovieStars")
fun RatingBar.setMovieStars(rating: Double) {
    this.rating = rating.toFloat() / 2
}

@BindingAdapter("image", "placeholder")
fun setImage(image: ImageView, url: String?, placeHolder: Drawable) {
    when (url.isNullOrBlank()) {
        true -> image.setImageDrawable(placeHolder)
        else -> {
            Glide.with(image.context).load("https://image.tmdb.org/t/p/w500$url").centerCrop()
                .placeholder(R.drawable.moviepedia_logo)
                .error(R.drawable.moviepedia_logo)
                .into(image)
        }
    }
}

@BindingAdapter("name", "title")
fun setText(textView: TextView, name: String?, title: String?) {
    when (title.isNullOrEmpty()) {
        true -> name?.let { textView.text = it }
        false -> textView.text = title
    }
}