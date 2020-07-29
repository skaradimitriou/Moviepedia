package com.stathis.moviepedia.recyclerviews

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.R

class PopularMoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val movieName: TextView = itemView.findViewById(R.id.movie_title)
    val movieRating: TextView = itemView.findViewById(R.id.movie_rating)
    val movieGenre: TextView = itemView.findViewById(R.id.movie_genre)
    val movieImg: ImageView = itemView.findViewById(R.id.movie_img)
}