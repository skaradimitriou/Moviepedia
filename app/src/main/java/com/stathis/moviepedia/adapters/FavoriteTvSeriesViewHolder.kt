package com.stathis.moviepedia.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stathis.moviepedia.R
import com.stathis.moviepedia.databinding.PopularItemRowBinding
import com.stathis.moviepedia.models.FavoriteTvSeries
import kotlin.math.roundToInt

class FavoriteTvSeriesViewHolder(var binding: PopularItemRowBinding) : RecyclerView.ViewHolder(binding.root) {

//    val tvSeriesName: TextView = itemView.findViewById(R.id.movie_title)
//    val tvSeriesImg: ImageView = itemView.findViewById(R.id.movie_img)
//
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