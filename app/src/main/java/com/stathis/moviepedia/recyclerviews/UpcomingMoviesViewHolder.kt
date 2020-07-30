package com.stathis.moviepedia.recyclerviews

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.Movies

class UpcomingMoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val upcom_movieImg: ImageView = itemView.findViewById(R.id.upcom_movieImg)
    val upcom_movie_title: TextView = itemView.findViewById(R.id.upcom_movie_title)

    fun bind(movies: Movies) {
        Glide.with(itemView.context)
            .load("https://image.tmdb.org/t/p/w500" + movies.backdrop_path)
            .into(upcom_movieImg)
        upcom_movie_title.text = movies.title
    }
}