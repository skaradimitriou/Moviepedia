package com.stathis.moviepedia.recyclerviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.MovieFeed
import com.stathis.moviepedia.models.Movies

class PopularMoviesAdapter(val popularMovies: MovieFeed) :
    RecyclerView.Adapter<PopularMoviesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMoviesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.popular_item_row, parent, false)
        return PopularMoviesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: PopularMoviesViewHolder, position: Int) {
        val currentItem = popularMovies.results.get(position)
        holder.bind(currentItem)
    }
}