package com.stathis.moviepedia.adapters

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stathis.moviepedia.R
import com.stathis.moviepedia.listeners.ItemClickListener
import com.stathis.moviepedia.models.Movies
import kotlinx.android.synthetic.main.popular_item_row.view.*
import kotlin.math.roundToInt

class PopularMoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(movies: Movies, listener: ItemClickListener) {
        if (movies.backdrop_path.isNullOrBlank()) {
            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w500" + movies.poster_path)
                .into(itemView.movie_img)
        } else {
            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w500" + movies.backdrop_path)
                .into(itemView.movie_img)
        }

        if (movies.title.isNullOrBlank()) {
            itemView.movie_title.text = movies.name
        } else {
            itemView.movie_title.text = movies.title
        }

        itemView.movie_genre.text = movies.media_type

        itemView.test_progressbar.progress = (movies.vote_average * 10).roundToInt()
        itemView.ratingTxt.text = movies.vote_average.toString()

        itemView.setOnClickListener {
            listener.onItemClick(movies)
        }
    }

}