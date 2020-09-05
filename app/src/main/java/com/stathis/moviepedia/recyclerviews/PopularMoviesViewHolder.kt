package com.stathis.moviepedia.recyclerviews

import android.content.Intent
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stathis.moviepedia.IntroScreen
import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.Movies

class PopularMoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val movieName: TextView = itemView.findViewById(R.id.movie_title)
    val movieGenre: TextView = itemView.findViewById(R.id.movie_genre)
    val movieImg: ImageView = itemView.findViewById(R.id.movie_img)
    val movieRating:TextView = itemView.findViewById(R.id.ratingTxt)

    fun bind(movies: Movies,listener: ItemClickListener) {

        if(movies.backdrop_path.isNullOrBlank()){
            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w500" + movies.poster_path)
                .into(movieImg)
        }else {
            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w500" + movies.backdrop_path)
                .into(movieImg)
        }

        if(movies.title.isNullOrBlank()){
            movieName.text = movies.name
        } else {
            movieName.text = movies.title
        }

        movieGenre.text = movies.media_type
        movieRating.text = movies.vote_average.toString()

        itemView.setOnClickListener {
            listener.onItemClick(movies)
        }
    }

}