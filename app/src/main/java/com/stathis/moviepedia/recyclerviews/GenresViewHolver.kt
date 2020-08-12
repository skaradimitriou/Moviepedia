package com.stathis.moviepedia.recyclerviews

import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.MovieGenres
import kotlinx.android.synthetic.main.genres_item_row.view.*

class GenresViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val genresItem: TextView = itemView.findViewById(R.id.genres)
    val genreColor:LinearLayout = itemView.findViewById(R.id.genreColor)

    fun bind(genres: MovieGenres,listener: GenresClickListener) {
        genresItem.text = genres.name

        when(genres.name){
            "Action" -> genreColor.setBackgroundColor(Color.parseColor("#4f5fef"))
            "Adventure" -> genreColor.setBackgroundColor(Color.parseColor("#23B993"))
            "Animation" -> genreColor.setBackgroundColor(Color.parseColor("#ff0045"))
            "Comedy" -> genreColor.setBackgroundColor(Color.parseColor("#f86611"))
            "Crime" -> genreColor.setBackgroundColor(Color.parseColor("#EC5657"))
            "Documentary" -> genreColor.setBackgroundColor(Color.parseColor("#2D2C4E"))
            "Drama" -> genreColor.setBackgroundColor(Color.parseColor("#000000"))
            "Family" -> genreColor.setBackgroundColor(Color.parseColor("#4f5fef"))
            "Fantasy" -> genreColor.setBackgroundColor(Color.parseColor("#23B993"))
            "History" -> genreColor.setBackgroundColor(Color.parseColor("#ff0045"))
            "Horror" -> genreColor.setBackgroundColor(Color.parseColor("#f86611"))
            "Music" -> genreColor.setBackgroundColor(Color.parseColor("#EC5657"))
            "Mystery" -> genreColor.setBackgroundColor(Color.parseColor("#2D2C4E"))
            "Romance" -> genreColor.setBackgroundColor(Color.parseColor("#000000"))
            "Science Fiction" -> genreColor.setBackgroundColor(Color.parseColor("#4f5fef"))
            "TV Movie" -> genreColor.setBackgroundColor(Color.parseColor("#23B993"))
            "Thriller" -> genreColor.setBackgroundColor(Color.parseColor("#ff0045"))
            "War" -> genreColor.setBackgroundColor(Color.parseColor("#f86611"))
            "Western" -> genreColor.setBackgroundColor(Color.parseColor("#EC5657"))
        }

        itemView.setOnClickListener{
            listener.onGenreClick(genres)
        }

    }

}