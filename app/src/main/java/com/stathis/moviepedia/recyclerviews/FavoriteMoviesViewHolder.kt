package com.stathis.moviepedia.recyclerviews

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.FavoriteMovies
import com.stathis.moviepedia.models.Movies

class FavoriteMoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val movieName: TextView = itemView.findViewById(R.id.movie_title)
    val movieImg: ImageView = itemView.findViewById(R.id.movie_img)

    fun bind(favoriteMovies: FavoriteMovies,listener: FavoriteClickListener) {
        Glide.with(itemView.context)
            .load("https://image.tmdb.org/t/p/w500" + favoriteMovies.photo)
            .into(movieImg)

        movieName.text = favoriteMovies.title

        itemView.setOnClickListener{
            listener.onFavoriteMoviesClick(favoriteMovies)
        }
    }
}