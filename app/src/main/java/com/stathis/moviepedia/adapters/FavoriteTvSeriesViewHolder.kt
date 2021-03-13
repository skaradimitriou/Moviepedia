package com.stathis.moviepedia.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stathis.moviepedia.databinding.PopularItemRowBinding
import com.stathis.moviepedia.listeners.FavoriteClickListener
import com.stathis.moviepedia.models.FavoriteTvSeries
import kotlin.math.roundToInt

class FavoriteTvSeriesViewHolder(var binding: PopularItemRowBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(favoriteTvSeries: FavoriteTvSeries, listener: FavoriteClickListener) {
        Glide.with(itemView.context)
            .load("https://image.tmdb.org/t/p/w500" + favoriteTvSeries.photo)
            .into(binding.movieImg)

        binding.ratingTxt.text = favoriteTvSeries.movie_rating.toString()
        binding.movieTitle.text = favoriteTvSeries.title
        binding.testProgressbar.progress = (favoriteTvSeries.movie_rating*10).roundToInt()

        itemView.setOnClickListener{
            listener.onFavoriteTvSeriesClick(favoriteTvSeries)
        }
    }

}