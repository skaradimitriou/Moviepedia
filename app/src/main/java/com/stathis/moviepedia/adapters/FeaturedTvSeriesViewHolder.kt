package com.stathis.moviepedia.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stathis.moviepedia.R
import com.stathis.moviepedia.listeners.ItemClickListener
import com.stathis.moviepedia.models.TvSeries

class FeaturedTvSeriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val upcom_movieImg: ImageView = itemView.findViewById(R.id.upcom_movieImg)
    val upcom_movie_title: TextView = itemView.findViewById(R.id.upcom_movie_title)

    fun present (tvSeries: TvSeries,listener: ItemClickListener) {
        Glide.with(itemView.context)
            .load("https://image.tmdb.org/t/p/w500" + tvSeries.poster_path)
            .into(upcom_movieImg)

        upcom_movie_title.text = tvSeries.name
        itemView.setOnClickListener{
            listener.onTvSeriesClick(tvSeries)
        }
    }
}