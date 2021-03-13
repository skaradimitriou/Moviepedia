package com.stathis.moviepedia.adapters

import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stathis.moviepedia.R
import com.stathis.moviepedia.listeners.ItemClickListener
import com.stathis.moviepedia.models.Movies
import kotlinx.android.synthetic.main.upcoming_movies_item_row.view.*

class UpcomingMoviesViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    
    fun bind(movies: Movies,listener: ItemClickListener) {
        Glide.with(itemView.context)
            .load("https://image.tmdb.org/t/p/w500" + movies.poster_path)
            .into(itemView.upcom_movieImg)

        //applying rating to the ratingBar
        itemView.ratingStars.rating = movies.vote_average.toFloat() / 2

        itemView.upcom_movie_title.text = movies.title
        itemView.setOnClickListener {
            listener.onItemClick(movies)
        }
    }
}