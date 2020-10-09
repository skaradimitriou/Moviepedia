package com.stathis.moviepedia.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stathis.moviepedia.databinding.PopularItemRowBinding
import com.stathis.moviepedia.models.FavoriteMovies
import kotlin.math.roundToInt

class FavoriteMoviesViewHolder(var binding:PopularItemRowBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(favoriteMovies: FavoriteMovies,listener: FavoriteClickListener) {
        Glide.with(itemView.context)
            .load("https://image.tmdb.org/t/p/w500" + favoriteMovies.photo)
            .into(binding.movieImg)

        binding.testProgressbar.progress = (favoriteMovies.movie_rating*10).roundToInt()
        binding.movieTitle.text = favoriteMovies.title
        binding.ratingTxt.text = favoriteMovies.movie_rating.toString()

        itemView.setOnClickListener{
            listener.onFavoriteMoviesClick(favoriteMovies)
        }
    }
}