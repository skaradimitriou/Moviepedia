package com.stathis.moviepedia.recyclerviews

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.TvSeries
import com.stathis.moviepedia.models.TvSeriesFeed

class AiringTvSeriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val movieName: TextView = itemView.findViewById(R.id.movie_title)
    val movieRating: TextView = itemView.findViewById(R.id.movie_rating)
    val movieGenre: TextView = itemView.findViewById(R.id.movie_genre)
    val movieImg: ImageView = itemView.findViewById(R.id.movie_img)

    fun bind(tvSeriesFeed: TvSeries,listener:ItemClickListener){
        Glide.with(itemView.context)
            .load("https://image.tmdb.org/t/p/w500" + tvSeriesFeed.backdrop_path)
            .into(movieImg)

        if(tvSeriesFeed.name.isNullOrBlank()){
            movieName.text = tvSeriesFeed.original_name
        } else {
            movieName.text = tvSeriesFeed.name
        }

        movieRating.text = tvSeriesFeed.vote_average.toString() + "/10"

        itemView.setOnClickListener{
            listener.onTvSeriesClick(tvSeriesFeed)
        }
    }
}