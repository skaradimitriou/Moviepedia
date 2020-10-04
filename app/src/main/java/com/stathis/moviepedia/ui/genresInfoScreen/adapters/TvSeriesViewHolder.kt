package com.stathis.moviepedia.ui.genresInfoScreen.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stathis.moviepedia.R
import com.stathis.moviepedia.adapters.ItemClickListener
import com.stathis.moviepedia.models.Movies
import com.stathis.moviepedia.models.TvSeries

class TvSeriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val resultImg: ImageView = itemView.findViewById(R.id.movie_img)
    val resultTxt: TextView = itemView.findViewById(R.id.movie_title)
    val movieRating: TextView = itemView.findViewById(R.id.ratingTxt)
    val movieGenre: TextView = itemView.findViewById(R.id.movie_genre)

    fun bind(tvSeries:TvSeries, listener: ItemClickListener) {

        if(tvSeries.poster_path.isNullOrEmpty()){
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w500" + tvSeries.backdrop_path)
                    .into(resultImg)
            }
            else {
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w500" + tvSeries.poster_path)
                    .into(resultImg)
            }

        Glide.with(itemView.context)
            .load("https://image.tmdb.org/t/p/w500" + tvSeries.poster_path)
            .into(resultImg)

        if (tvSeries.name.isNullOrBlank()) {
            resultTxt.text = tvSeries.original_name
        } else {
            resultTxt.text = tvSeries.name
        }

        movieRating.text = tvSeries.vote_average.toString()

        itemView.setOnClickListener {
            listener.onTvSeriesClick(tvSeries)
        }

    }
}