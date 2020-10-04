package com.stathis.moviepedia.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.FavoriteTvSeries

class FavoriteTvSeriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val tvSeriesName: TextView = itemView.findViewById(R.id.movie_title)
    val tvSeriesImg: ImageView = itemView.findViewById(R.id.movie_img)

    fun bind(favoriteTvSeries: FavoriteTvSeries, listener: FavoriteClickListener) {
        Glide.with(itemView.context)
            .load("https://image.tmdb.org/t/p/w500" + favoriteTvSeries.photo)
            .into(tvSeriesImg)

        tvSeriesName.text = favoriteTvSeries.title

        itemView.setOnClickListener{
            listener.onFavoriteTvSeriesClick(favoriteTvSeries)
        }
    }

}