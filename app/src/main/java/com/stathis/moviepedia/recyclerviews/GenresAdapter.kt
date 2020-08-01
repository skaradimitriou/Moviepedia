package com.stathis.moviepedia.recyclerviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.MovieGenres
import com.stathis.moviepedia.models.MovieGenresFeed

class GenresAdapter(val movieGenres: MovieGenresFeed) : RecyclerView.Adapter<GenresViewHolver>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenresViewHolver {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.genres_item_row, parent, false)
        return GenresViewHolver(view)
    }

    override fun getItemCount(): Int {
        return movieGenres.genres.size
    }

    override fun onBindViewHolder(holder: GenresViewHolver, position: Int) {
        val currentItem = movieGenres.genres.get(position)
        holder.bind(currentItem)
    }
}