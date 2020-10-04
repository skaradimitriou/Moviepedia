package com.stathis.moviepedia.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.MovieGenres

class GenresViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val genresItem: TextView = itemView.findViewById(R.id.genres)

    fun bind(genres: MovieGenres,listener: GenresClickListener) {
        genresItem.text = genres.name

        when(genres.name){
            "Action" -> genresItem.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4f5fef"))
            "Adventure" -> genresItem.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#23B993"))
            "Animation" -> genresItem.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ff0045"))
            "Comedy" -> genresItem.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#f86611"))
            "Crime" -> genresItem.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#EC5657"))
            "Documentary" -> genresItem.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#2D2C4E"))
            "Drama" -> genresItem.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            "Family" -> genresItem.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4f5fef"))
            "Fantasy" -> genresItem.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#23B993"))
            "History" -> genresItem.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ff0045"))
            "Horror" -> genresItem.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#f86611"))
            "Music" -> genresItem.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#EC5657"))
            "Mystery" -> genresItem.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#2D2C4E"))
            "Romance" -> genresItem.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            "Science Fiction" -> genresItem.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4f5fef"))
            "TV Movie" -> genresItem.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#23B993"))
            "Thriller" -> genresItem.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ff0045"))
            "War" -> genresItem.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#f86611"))
            "Western" -> genresItem.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#EC5657"))
        }

        itemView.setOnClickListener{
            listener.onGenreClick(genres)
        }

    }

}