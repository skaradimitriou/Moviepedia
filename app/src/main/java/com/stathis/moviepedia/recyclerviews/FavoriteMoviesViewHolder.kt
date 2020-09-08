package com.stathis.moviepedia.recyclerviews

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stathis.moviepedia.databinding.PopularItemRowBinding
import com.stathis.moviepedia.models.FavoriteMovies

class FavoriteMoviesViewHolder(var binding:PopularItemRowBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(favoriteMovies: FavoriteMovies,listener: FavoriteClickListener) {
        Glide.with(itemView.context)
            .load("https://image.tmdb.org/t/p/w500" + favoriteMovies.photo)
            .into(binding.movieImg)

        binding.movieTitle.text = favoriteMovies.title

        itemView.setOnClickListener{
            listener.onFavoriteMoviesClick(favoriteMovies)
        }
    }
}